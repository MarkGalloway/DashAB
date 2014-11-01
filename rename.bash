#!/bin/bash 

search_dir () {
	for curFile in "$1/"*; do
		if [[ -d $curFile ]]; then
			echo "$curFile is a directory"
			search_dir "${curFile}"
		elif [[ -f $curFile ]]; then
		    	echo "$curFile is a file"
			extension="${curFile##*.}"
			filename="${curFile%.*}"
			if [ "${extension}" = "db" ]; then
				echo "${filename}.ds"
				git mv -- "${filename}.db" "${filename}.ds"
			fi
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
	echo "${_dir}"
	search_dir "${_dir}"
done



