#!/bin/bash

export PATH="/usr/local/bin:/usr/bin:$JAVA_HOME/bin:$MVN_HOME/bin:$PATH"
sedi() {
  case $(uname) in
    Darwin*) sedi=('-i' '') ;;
    *) sedi='-i' ;;
  esac
  sed "${sedi[@]}" "$@"
}

type=$1
releaseVersion=$2
nextDevelopVersion=$2
branchVersion=$3
modifyDepenOnVersions=$4
echo "type------>$type"
echo "releaseVersion------>$releaseVersion"
echo "branchVersion------>$branchVersion"

function updateDependenciesVersion() {
  currPwd=`pwd`
  project=`echo $currPwd|sed 's;.*/;;g'`
  #version=`cat pom.xml |grep "<version>"|sed -n '1p'|sed "s;</\?version>;;g"|sed 's;^\s*;;g'`
  version=$1
  echo "$project current version is $version"

  dependencyProjectVersion=${project}-version
  #echo $dependencyProjectVersion
  projects="xpay-gateway xpay-external-gateway account-server configuration-server merchant-server operation-server payment-server external-server openapi-sdk zerofinance-commons"

  cd ..
  for project in $projects
  do
    #echo "Replacing $project..."
    #cd ../$project
    pomFile=`grep -r "$dependencyProjectVersion" $project | awk -F ':' '{print $1}'`
    if [[ "$pomFile" != "" ]]; then
      fullPomFile=$pomFile
      echo "Replacing dependency project version: "$fullPomFile
      sed -i "s;.*<$dependencyProjectVersion>.*;		<$dependencyProjectVersion>$version</$dependencyProjectVersion>;g" $fullPomFile
    fi
    #cd - > /dev/null
  done
  cd $currPwd
  echo "All done, please double-check."
}

#Working for deploy: deploying jar to maven before pushing codes to git
if [[ "$type" == "deploy" ]]; then
    echo "Working for deploy: $releaseVersion..."
    echo "Deploying this project to maven repository: $releaseVersion..."
    mvn clean deploy -Dmaven.test.skip=true

    if [[ "$modifyDepenOnVersions" == "true" ]]; then
      echo "modifyDepenOnVersions------>$modifyDepenOnVersions"
      echo "Replacing all of the release versions depended on ${releaseVersion}..."
      #替换版本
      updateDependenciesVersion $releaseVersion
    fi
fi

#Working for afterChangeNextVersion
#if [[ "$type" == "afterChangeNextVersion" ]]; then
#    echo "Working for afterChangeNextVersion: $nextDevelopVersion..."
#      echo "modifyDepenOnVersions------>$modifyDepenOnVersions"
#      echo "Replacing all of the snapshot versions depended on ${nextDevelopVersion}..."
#
#      #替换版本
#      updateDependenciesVersion $nextDevelopVersion
#fi