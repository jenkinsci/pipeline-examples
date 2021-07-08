#!/bin/bash
#
# Hashicorp packer/terraform simple stack build wrapper
# * facilitates building base & service/app AMI (service/app depends on base)
# * tag AMI's with SHA1 of the packer .json file that built it
#
# Wrapping a few CLI command in bash always seems like a good idea at the start.
# It's not. Don't do it. Use python to wrap & possible call API's directly.

# Exit immediately if a command exits with a non-zero status
set -e

# debug - expand all commands
# set -x

# load our helper functions
source scripts/common.sh

# check that the tools we require are present
package_check

#
# base.sh DIR TARGET [BASE_NAME]
DIR="$1"
NAME="$2"
BASE_NAME="$3"
if [[ -z "$DIR" ]]; then
    echo "please specify the directory as first runtime argument"
    exit 1
fi
if [[ -z "$NAME" ]]; then
    echo "please specify the name as second runtime argument"
    exit 1
fi
if [[ -z "$BASE_NAME" ]]; then
    echo "No base AMI given"
else
    export BASE_BUILT=$(base_rebuilt $BASE_NAME)
    if [ "${BASE_BUILT}" = "true" ]; then
        echo "Couldn't find ${BASE_NAME} in manifest-${BASE_NAME}.json, looking up AMI via EC2 API"
    fi
    export AMI_BASE="$(get_base_ami "$BASE_BUILT" "$BASE_NAME" "$BASE_NAME")"
fi

export SHA=$(git ls-tree HEAD "$DIR" | cut -d" " -f3 | cut -f1)
TAG_EXISTS=$(tag_exists $SHA)

if [ "$TAG_EXISTS" = "false" ]; then
    echo "No AMI found for ${NAME} (SHA: ${SHA}), building one.."
    packer build ${DIR}/$NAME.json
    PACKER_EXIT=$?
    echo "Packer exit code: ${PACKER_EXIT}"
else
    echo "AMI found for ${NAME} (SHA: ${SHA})"
    touch manifest-${NAME}.json
fi
