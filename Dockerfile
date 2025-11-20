FROM tomcat:9.0-jdk17

# Remove default Tomcat ROOT app
RUN rm -rf /usr/local/tomcat/webapps/ROOT

# Copy built WAR file
COPY dist/enrollment.war /usr/local/tomcat/webapps/ROOT.war

# Copy DB context template (correct repo path)
COPY ENROLLMENT/META-INF/context-template.xml /tmp/context-template.xml

# Add entrypoint
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["catalina.sh", "run"]
