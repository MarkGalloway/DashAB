#!/bin/bash 

search_dir () {
	for curFile in "$1/"*; do
		if [[ -d $curFile ]]; then
			folder="${curFile##*/}"
			cp -r ${curFile} "AllTests/$1${folder}"
		elif [[ -f $curFile ]]; then
		    	echo "$curFile is a file"
		else
		    	echo "$curFile is not valid"
		    	exit 1
		fi
		
		#echo "$curFile"
		#echo "${curFile:0:-1}s"
	done
}

pattern="Test"
for _dir in "${pattern}"*; do
	mkdir "AllTests"
	echo "${_dir}"
	search_dir "${_dir}"
done
