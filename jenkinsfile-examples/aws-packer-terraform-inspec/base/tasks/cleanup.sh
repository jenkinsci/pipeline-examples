#!/bin/bash
set -e

echo "---- cleanup"
echo Ubuntu Provision Cleanup
sudo DEBIAN_FRONTEND=noninteractive apt-get -y autoremove --purge
sudo DEBIAN_FRONTEND=noninteractive apt-get -y autoclean
sudo DEBIAN_FRONTEND=noninteractive apt-get    check

sudo rm -rf /var/lib/apt/lists/*
sudo rm -rf /tmp/*
