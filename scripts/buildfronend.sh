# Build script for front-end (siblink.com)
# It should place in home folder /home/ec2-user
#!/bin/bash
cd saatya
git pull
grunt build:pro
cd build
echo "Copy files from folder build to apache folder"
sudo cp -r * /var/www/html/
exit