brot
======

Step 1: install nodejs   

  * http://nodejs.org/download/

Step 2: checkout branch jsclient and install grunt and orther dependencies in brot folder

  ```
  git checkout jsclient
  npm install -g grunt-cli
  npm install
  ```
Step 3: Run server front-end
```
node server.js
```

Step 4: Watching file changes

```
grunt watch
```

Access: http://localhost:9000



### Location of assets


Assets folder (fonts, images)
```
src/assets
```

CSS code
```
src/less
```

Javascript app
```
src/app/**/*.js
```

Templates
```
template
```

### Development process

Run server
```
node server.js
```

Open new terminal
```
grunt watch
```

If there are any changes `grunt` task will copy to `build` folder (Do not edit file in `build` folder).
_But if you want to add new files, you will need to run `grunt watch` again._



# How to deploy in server

```
ssh -i demo.pem ec2-user@54.213.94.216
```

In home folder of ec2-user

```
./deploy.sh
```

Done!

