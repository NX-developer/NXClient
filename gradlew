#!/usr/bin/env sh

APP_HOME=`dirname "$0"`
APP_HOME=`cd "$APP_HOME"; pwd`

JAVA_EXE=java
command -v java >/dev/null 2>&1 || {
    if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
        JAVA_EXE="$JAVA_HOME/bin/java"
    else
        echo "ERROR: No 'java' found. Set JAVA_HOME to a Java 8 JDK." >&2
        exit 1
    fi
}

CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar:$APP_HOME/gradle/wrapper/gradle-cli.jar"

exec "$JAVA_EXE" $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS \
  "-Dorg.gradle.appname=gradlew" \
  -classpath "$CLASSPATH" \
  org.gradle.wrapper.GradleWrapperMain "$@"
