#!/bin/bash
set -e

echo "---- debug info"
uname -a
cat /etc/os-release
dpkg -l | grep linux-
