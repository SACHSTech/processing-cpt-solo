[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/eALKwJKC)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=13282448&assignment_repo_type=AssignmentRepo)
# Those who remain Game

Those who Remain is a 2D retro style zombie game. 
The year is 2028 and the E.Fungi mushroom has escaped from a lab and infected the whole world, converting them into bloodthirsty E.Fungoids! You are one of the few survivors who remain. 

While you were driving to Toronto, your car broke down in the middle of a road and the E.Fungoids are hungry. A helicopter is coming to rescue you but it will take some time arrive. 
Can you defend yourself for long enough will you be overrun by the hordes of infected?

CONTROLS:

WASD - Movement.
W - up
A - left
S - down
D - right
You can move anywhere around the map except through cars. 

Don't get hit by the zombies, they do damage. If you reach 0 hp, you will die. 
Your health will regenerate slowly.

Click your mouse to shoot bullets. 
Before wave 5, you have a semi-automatic rifle which means you fire each bullet individually. 
At wave 5, you will unlock an automatic rifle which allows you to hold down your mouse button and fire the bullets continuously. 

Your reload speed and accuracy will improve after each wave. 

Zombies take 3 bullets to kill. 

Survive 10 waves to win!
After you win, you can choose to play in freeplay mode where you try to survive as many waves as possible. 
Your score is the wave you survive to. 


LIMITATIONS:

- Zombies go right through the cars. I do not know how to code the zombie pathfinding AI so for now, it is a feature. 
- Game crashes after a while of playing. Apparently, you cannot pushMatrix more than 32 times so if you play for too long or do too many rotations, the game will crash. There seems to be no way around this. Luckily, you should be able to finish a full playthrough of 10 waves without any problems. 
