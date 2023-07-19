docker build -t helper:v1 . 
docker save -o helper.tar helper:v1
docker load < helper.tar
docker run -p 10000:10000 -v /home/git/docs/devDoc/:/app/uploadfile --name helper -d helper:v1

test

