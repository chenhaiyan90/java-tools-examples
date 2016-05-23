date=`date +%s`
openssl genrsa -out rsa_private_key_$date.pem 1024
openssl rsa -in rsa_private_key_$date.pem -pubout -out rsa_public_key_$date.pem
openssl pkcs8 -topk8 -inform PEM -in rsa_private_key_$date.pem -outform PEM -nocrypt -out rsa_private_key_pkcs8_$date.pem
