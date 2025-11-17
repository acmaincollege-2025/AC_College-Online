FROM tomcat:9.0-jdk17

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8 TZ=Asia/Manila

# remove default ROOT app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# copy the war produced by Ant
COPY dist/enrollment.war /usr/local/tomcat/webapps/ROOT.war

# copy context template and entrypoint into image
COPY META-INF/context-template.xml /tmp/context-template.xml
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
