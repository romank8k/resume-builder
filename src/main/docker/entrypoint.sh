#!/bin/bash

if [ "$1" != 'resume-generator' ]; then
    exec "$@"
fi

shift  # Remove 'resume-generator' from the args.
exec java -cp 'resume-generator.jar:lib/*' me.romankh.resumegenerator.cmd.StartResumeGeneratorServer "$@"