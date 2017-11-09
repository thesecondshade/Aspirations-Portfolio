#!/bin/bash

read -p "Delete $1? [y/n] " -n 1 -r
echo    # (optional) move to a new line
if [[ $REPLY =~ ^[Yy]$ ]]
then
    echo "Deleting $1..."
    aws cloudformation delete-stack --stack-name $1
fi
