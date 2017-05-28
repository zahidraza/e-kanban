difference in dev and production mode:
1.  src/js/index.js
    : dev
        (function () {
          window.baseUrl = 'http://localhost:8000';
          window.serviceHost = "http://localhost:8000/api";
        })();
    : production
        (function () {
          const baseUrl = location.protocol+'//'+location.hostname+(location.port ? ':'+location.port: '');
          window.baseUrl = baseUrl + '/ekanban';
          window.serviceHost = baseUrl + "/ekanban/api";
        })();

2.  src/main/webapp/WEB-INF/index.html
    dev:
        <script src="/static/index.js"></script>
    production:
        <script src="/ekanban/static/index.js"></script>

3.  src/main/resources/application.properties
    //Change accordingly to match your system credential
    spring.datasource.url = jdbc:mysql://localhost:3306/ekanban
    spring.datasource.username = root
    spring.datasource.password = zahid