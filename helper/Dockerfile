FROM python
LABEL maintainer="ChenHuaYou<chen.hua.you@163.com>"
COPY ./web/ /app/web/

WORKDIR /app/web
RUN pip install -r requirements.txt -i http://mirrors.aliyun.com/pypi/simple --trusted-host mirrors.aliyun.com


EXPOSE 10000
CMD ["python", "server.py"]
