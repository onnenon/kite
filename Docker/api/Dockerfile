FROM  python:3.6-alpine
LABEL author="Stephen Onnen"


ADD Backend /opt 

WORKDIR /opt

RUN \
    apk add --no-cache postgresql-libs && \
    apk add --no-cache --virtual .build-deps libffi-dev gcc musl-dev postgresql-dev && \
    python3 -m pip install -r requirements.txt --no-cache-dir && \
    python3 -m pip install -e . &&\
    apk --purge del .build-deps

EXPOSE 5000

CMD ["gunicorn", "-w 2", "-b 0.0.0.0:5000", "forum_api:app"]
