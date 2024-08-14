sudo dnf install -y git
sudo dnf install -y java-11-openjdk.x86_64
sudo dnf install -y bash
sudo dnf install -y curl
sudo dnf install -y rlwrap
sudo dnf install -y emacs
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
chmod +x linux-install.sh
sudo ./linux-install.sh
rm linux-install.sh
