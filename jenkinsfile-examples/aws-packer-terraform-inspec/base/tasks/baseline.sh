#!/bin/bash
set -e

echo "---- set locale"
sudo locale-gen C.UTF-8 || true
sudo update-locale LANG=en_US.UTF-8
sudo /bin/bash -c 'echo "export LANG=C.UTF-8" >> /etc/skel/.bashrc'

echo "---- make Apt non interactive"
sudo /bin/bash -c 'echo "force-confnew" >> /etc/dpkg/dpkg.cfg'
#sudo /bin/bash -c 'cat /tmp/dpkg.cfg.update >> /etc/sudoers.d/env_keep'
#sudo cp /tmp/apt.conf.update /etc/apt/apt.conf

echo "---- Update and Upgrade"
sudo DEBIAN_FRONTEND=noninteractive apt-get -y update
sudo DEBIAN_FRONTEND=noninteractive apt-get -y upgrade
sudo DEBIAN_FRONTEND=noninteractive apt-get -y install apt-transport-https
sudo DEBIAN_FRONTEND=noninteractive apt-get -y install curl unzip zip jq
