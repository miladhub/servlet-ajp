Sample Servlet-AJP integration
===

Sample servlet reading an `HttpServletRequest` attribute set by Apache as environment variable and
processed as an attribute by the `mod_proxy_ajp` Apache module.

The AJP integration is currently working on JBoss EAP 7.3.0, but it does *not* work
on neither Wildfly 24.0.1 nor JBoss EAP 7.4.0.

How it should work
===

Apache sets an "environment variable" via `SetEnv`:

```conf
SetEnv AJP_uid foo
```

Environment variables that are prefixed by the "AJP_" string are processed by the `mod_proxy_ajp` Apache
module and put as `HttpServletRequest` attributes, see <https://httpd.apache.org/docs/2.4/mod/mod_proxy_ajp.html#env>.

At this point, the servlet can read the `uid` attribute from the incoming request:

```java
request.getAttribute("uid")
```

In this sample project, the JavaScript code displays the `uid` on the page:

```javascript
document.getElementById("uid").innerHTML = json.uid;
```

Installing Apache with AJP
===

```bash
docker rm -f apache-ajp 
docker run -dit --name apache-ajp -p 80:80 httpd:2.4
docker cp httpd.conf apache-ajp:/usr/local/apache2/conf
docker exec apache-ajp apachectl restart
```

Deploying the servlet
===

[Download JBoss EAP 7.4.0](https://developers.redhat.com/content-gateway/file/jboss-eap-7.4.0.zip)
and extract it to `~/jboss-eap-7.4/`.

Issue the following command from a CLI tab:

```bash
export JBOSS_HOME=~/jboss-eap-7.4 && $JBOSS_HOME/bin/standalone.sh
```

Issue the following command from another tab:

```bash
export JBOSS_HOME=~/jboss-eap-7.4 && $JBOSS_HOME/bin/jboss-cli.sh
[disconnected /] connect
[standalone@localhost:9990 /] /socket-binding-group=standard-sockets/socket-binding=ajp:add(port=8009)
{
    "outcome" => "failed",
    "failure-description" => "WFLYCTL0212: Duplicate resource [
    (\"socket-binding-group\" => \"standard-sockets\"),
    (\"socket-binding\" => \"ajp\")
]",
    "rolled-back" => true
}

[standalone@localhost:9990 /] /subsystem=undertow/server=default-server/ajp-listener=myListener:add(socket-binding=ajp, scheme=http, enabled=true)
{"outcome" => "success"}
```

Build and deploy the servlet:

```bash
export JBOSS_HOME=~/jboss-eap-7.4 && mvn clean install && cp target/mycalcwebapp.war $JBOSS_HOME/standalone/deployments
```

Accessing the app
===

Open up <http://localhost/mycalcwebapp/> and click on the "Show uid" submit button.
If it works, the value of the `uid` variable, "foo", is shown at the bottom of the page.