# Projet de Réseau - 2023 - Groupe 2 


## Auteurs

**Bertaux Titouan,**
**Lecomte Arthur,**
**Levesque Kyliann,**
**Cnaepelnick Evan**

## Structure du projet

3 fichiers principaux pour la 1ère version

  * `ChatServer.java` : Correspond au serveur de chat nécessaire pour créer un serveur sur un port et une adresse IP. Ilest possible pour les clients de s'y connecter, démarrer une musique, l'arrêter ou encore de discuter en direct cependant il y a un retard au niveau de l'envoi des messages. 

  * `ChatClient.java` : Correspond au client de chat nécessaire pour créer un client

  * `GestionSon.java` : Correspond au gestionnaire de son nécessaire pour la lecture des fichiers audio

Ensuite pour la 2nde version :

  * `GroupChat.java` : Regroupe serveur et client au sein du même fichier à l'aide d'une adresse ip type broadcast et un port. L'ensemble des clients peuvent se connecter, discuter à l'aide d'un chat en direct et lancer une musique. Celui-ci est entièrement fonctionnel et regroupe toutes les consignes demandées dans le cadre de ce projet

  * `GestionSon.java` : Correspond au gestionnaire de son nécessaire pour la lecture des fichiers audio

## Axes possibles d'amélioration

  * `Version 1` : Amélioration de l'envoi des messages à travers une ArrayList et une socket. Meilleure gestion de la musique à travers plus de commandes et fonctionnalités.

  * `Version 2` : Améliorer la gestion du son en ajoutant quelques commandes améliorant l'interface utilisateur. Optimiser notre code dans l'idée de l'utiliser sur un serveur distant.
  
## Lien drive dossier

  https://drive.google.com/file/d/137TGbRUpqRiPopdECGj_aV7KmwYMx8Ed/view
