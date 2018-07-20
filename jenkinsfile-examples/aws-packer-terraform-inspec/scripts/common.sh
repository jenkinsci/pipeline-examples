#!/bin/bash
#
# Wrapping a few CLI command in bash always seems like a good idea at the start.
# It's not. Don't do it. Use python to wrap & possible call API's directly.
tag_exists () {
    local SHA=$1
    if [[ -z "$SHA" ]]; then
        echo "-- ERROR: there was a problem looking up AMI by sha"
        exit 1
    fi
    EMPTY=$(aws ec2 describe-images --filters Name=tag:SHA,Values=$SHA --query 'Images[*]')
    AWS_CLI_EXIT_CODE=$?
    if [[ "${AWS_CLI_EXIT_CODE}" -eq 0 ]]; then
      if [ "$EMPTY" = "[]" ]; then
          echo "false"
      else
          echo "true"
      fi
    else
      (>&2 echo "ERROR: AWS CLI error checking for existing images matching ${SHA}")
      exit 2
    fi
}

get_git_branch () {
# output the current branch, handling detached HEAD as found in Jenkins
# https://stackoverflow.com/questions/6059336/how-to-find-the-current-git-branch-in-detached-head-state
    local GIT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

    # Jenkins will often checkout the SHA of a branch, (detached HEAD)
    if [[ "${GIT_BRANCH}" == 'HEAD' ]]; then
      # lookup branch against remotes, without network access (we may not have creds to talk to git remote)
      echo "$(git branch --remote --verbose --no-abbrev --contains | sed -Ene 's/^[^\/]*\/([^\ ]+).*$/\1/p')"
    else
      echo "${GIT_BRANCH}"
    fi
}

base_rebuilt () {
    local NAME=$1
    if [[ -e "manifest-$NAME.json" ]] && [[ -s "manifest-$NAME.json" ]]; then
        echo "true"
    else
        echo "false"
    fi
}

extract_artifact_id () {
    local NAME="$1"
    local AMI="$(cat manifest-$NAME.json | jq '.builds[0].artifact_id' | perl -n -e'/us-east-1:(ami-[a-z0-9]+)/ && print $1')"
    echo "${AMI}"
}

get_base_ami () {
    local BASE_BUILT=$1
    local DIR=$2
    local NAME=$3
    if [ "$BASE_BUILT" = "false" ]; then
        EXISTING_BASE_SHA="$(git ls-tree HEAD $DIR | cut -d" " -f3 | cut -f1)"
        EXISTING_BASE_IMAGE=$(aws ec2 describe-images --filters Name=tag:SHA,Values=$EXISTING_BASE_SHA --query 'Images[*]' | jq -r '.[0].ImageId')
        echo "$EXISTING_BASE_IMAGE"
    else
        BASE_AMI_US_EAST_1="$(extract_artifact_id $NAME)"
        echo "${BASE_AMI_US_EAST_1}"
    fi
}

package_check () {
    command -v aws > /dev/null || (echo "aws cli must be installed" && exit 1)
    command -v packer > /dev/null || (echo "packer must be installed" && exit 1)
    command -v terraform > /dev/null || (echo "packer must be installed" && exit 1)
    command -v git > /dev/null || (echo "git must be installed" && exit 1)
    command -v jq > /dev/null || (echo "jq must be installed" && exit 1)
    command -v perl > /dev/null || (echo "perl must be installed" && exit 1)
}

check_terraform_version() {
  # TODO: extract from requirements.txt or something?
  TERRAFORM_REQUIRED_VERSION="v0.11.7"

  TERRAFORM_BIN=$(which terraform)
  TERRAFORM_INSTALLED_VERSION=$(${TERRAFORM_BIN} -version | awk '/^Terraform/{ print $2 }')

  if [[ "${TERRAFORM_INSTALLED_VERSION}" != "${TERRAFORM_REQUIRED_VERSION}" ]]; then
    log "ERROR: ${TERRAFORM_BIN} is reporting ${TERRAFORM_INSTALLED_VERSION}, ${TERRAFORM_REQUIRED_VERSION} required, aborting."
    exit 1
  fi
}

check_aws_credentials () {
    [[ -z "${AWS_DEFAULT_REGION}" ]] && (echo "AWS_DEFAULT_REGION must be set" && exit 1)
    [[ -z "${AWS_ACCESS_KEY_ID}" ]] && (echo "AWS_ACCESS_KEY_ID must be set" && exit 1)
    [[ -z "${AWS_SECRET_ACCESS_KEY}" ]] && (echo "AWS_SECRET_ACCESS_KEY must be set" && exit 1)
    [[ 1 ]]
}

generate_terraform_backend() {
    # inspired by https://github.com/hashicorp/terraform/issues/12877#issuecomment-311649591
    local PROJECT_NAME
    local ACCOUNT_ID
    local LOCATION_CONSTRAINT
    local BUCKET_NAME
    local BUCKET_EXISTS
    local TABLE_INDEX
    local TABLE_NAME

    if [[ -z "$1" ]]; then
        PROJECT_NAME="${PWD##*/}" # use current dir name
    else
        PROJECT_NAME=$1
    fi
    ACCOUNT_ID="$(aws sts get-caller-identity --query Account --output text)"

    if [[ "${AWS_DEFAULT_REGION}" = "us-east-1" ]]; then
        LOCATION_CONSTRAINT=""
    else
        LOCATION_CONSTRAINT='--create-bucket-configuration LocationConstraint="${AWS_DEFAULT_REGION}"'
    fi

    BUCKET_NAME="terraform-tfstate-${ACCOUNT_ID}"
    BUCKET_EXISTS=$(aws s3api list-buckets | jq ".Buckets[] | select(.Name == \"${BUCKET_NAME}\")")
    if [[ -z "${BUCKET_EXISTS}" ]]; then
        echo "Creating Terraform State S3 Bucket ${BUCKET_NAME} in ${AWS_DEFAULT_REGION}"
        aws s3api create-bucket \
            --region "${AWS_DEFAULT_REGION}" \
            ${LOCATION_CONSTRAINT} \
            --bucket "${BUCKET_NAME}"
    fi

    TABLE_NAME="terraform_locks"
    TABLE_INDEX=$(aws dynamodb list-tables | jq ".TableNames | index(\"${TABLE_NAME}\")")
    if [[ "${TABLE_INDEX}" = 'null' ]];then
        echo "Creating Terraform State DynamoDB Lock Table ${TABLE_NAME} in ${AWS_DEFAULT_REGION}"
        aws dynamodb create-table \
            --region "${AWS_DEFAULT_REGION}" \
            --table-name ${TABLE_NAME} \
            --attribute-definitions AttributeName=LockID,AttributeType=S \
            --key-schema AttributeName=LockID,KeyType=HASH \
            --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1
        aws dynamodb wait table-exists --table-name terraform_locks
    fi


    # NB - the pattern of managing the S3 bucket & DynamoDB table in Terraform
    # makes it impossible to cleanly destroy the terraform stack, so we don't do that
    cat <<EOF > ./backend_config.tf
terraform {
  backend "s3" {
    bucket         = "${BUCKET_NAME}"
    key            = "${PROJECT_NAME}"
    region         = "${AWS_DEFAULT_REGION}"
    dynamodb_table = "terraform_locks"
  }
}
EOF
}

map_branch_to_workspace() {
  # TODO input & output sanity checking..
  if [[ $1 = 'master' ]]; then
    echo "default" | tr / -
  else
    echo $1 | tr / -
  fi
}

map_branch_to_tfvars() {
    # map the branch to a tfvars file, with some sensible defaults
    local TF_VARS_FILE
    case "$1" in
        master)
            TF_VARS_FILE=master.tfvars
            ;;
        develop)
            TF_VARS_FILE=develop.tfvars
            ;;
        *)
            if [[ -f "$1".tfvars ]]; then
                TF_VARS_FILE="$1".tfvars
            else
                TF_VARS_FILE="testing-defaults.tfvars"
            fi
            ;;
    esac

    if [[ ! -f "${TF_VARS_FILE}" ]]; then
        touch "${TF_VARS_FILE}"
    fi

    echo "${TF_VARS_FILE}"
}
