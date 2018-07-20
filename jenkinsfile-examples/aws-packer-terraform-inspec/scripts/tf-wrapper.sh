#!/bin/bash
#
# terraform wrapper
#
# * takes care of pre "terraform init" steps (S3 & DynamoDB setup)
# * creates a terraform workspace to match the branch (master==default)
# * destroy workspace after destroying a stack
#
# Use case: build an ephemeral terraform configuration in CI to test & then destroy
# Use Case: Maintain default/master safely (how?!?)
#
set -e
# DEBUG
#set -x

THIS_SCRIPT=${BASH_SOURCE[0]:-$0}
# grumble, moan, PATH, symlinks
if [[ -L "${THIS_SCRIPT}" ]]; then
  THIS_SCRIPT=`readlink ${THIS_SCRIPT} 2>&1`
fi
PROJECT_HOME="$( cd "$( dirname "${THIS_SCRIPT}" )/.." && pwd )"

# load our helper functions
source ${PROJECT_HOME}/scripts/common.sh

# default to plan, to show changes, valid opions are plan, apply & destroy
TF_ACTION=plan

OPTIND=1 # Reset is necessary if getopts was used previously in the script.  It is a good idea to make this local in a function.
while getopts "a:e:hv" opt; do
    case "$opt" in
        a)  TF_ACTION=${OPTARG}
            ;;
        *)
            show_help
            exit 1
            ;;
    esac
done
shift "$((OPTIND-1))" # Shift off the options and optional --.

# check that the tools we require are present
package_check

# check that we have AWS credentials
check_aws_credentials

GIT_BRANCH=$(get_git_branch)
TF_WORKSPACE=$(map_branch_to_workspace ${GIT_BRANCH})
TF_VARS_FILE=$(map_branch_to_tfvars ${GIT_BRANCH})

# create the S3 bucket, DynamoDB & matching backend.tf
generate_terraform_backend

[[ ! -d .terraform ]] && terraform init
# the workspace may already exist - safe to ignore & carry on
terraform workspace new ${TF_WORKSPACE} || true
echo "Selecting workspace: ${TF_WORKSPACE}"
terraform workspace select ${TF_WORKSPACE}
case "${TF_ACTION}" in
    plan)
        [[ ! -d plan ]] && mkdir plan
        terraform plan -var-file=${TF_VARS_FILE} -out=plan/plan.out
        ;;
    apply)
        terraform apply plan/plan.out
        terraform output
        # once more for the camera
        terraform output -json > output.json
        ;;
    destroy)
        terraform destroy -var-file=${TF_VARS_FILE} -auto-approve
        terraform workspace select default
        terraform workspace delete ${TF_WORKSPACE}
        ;;
esac

echo "Done."
