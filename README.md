# Infinite Grid Exploration algorithm visualization

A program visualizing the execution of infinite grid exploration algorithms in triangular and hexagonal grids.
Algorithms $`\mathcal{A}^▲_1`$, $`\mathcal{A}^▲_2`$, $`\mathcal{A}^▲_3`$ and $`\mathcal{A}^⬣_1`$ are implemented,
as well as the moving group in the hexagonal grid under visibility range one.


## Run instructions

Build with
`./mvnw clean javafx:jlink` or `mvn clean javafx:jlink` if maven is installed,
then run with `./target/app/bin/app`.


## Algorithms

Algorithms can be switched by pressing the digit or numpad keys. Below is the list of corresponding executions.
Visibility ranges are listed first, as they are not clear from the visualization.
1) $`\mathcal{A}^▲_1`$, visibility range two, uses eight robots of a single color
2) $`\mathcal{A}^▲_2`$, visibility range one, uses six robots of two colors
3) $`\mathcal{A}^▲_3`$, visibility range one, uses five robots of three colors
4) $`\mathcal{A}^⬣_1`$, visibility range two, uses five robots of three colors 
5) Moving group in the hexagonal grid under visibility range one. Uses six robots of three colors


## Controls

| Action                        | Keys                                                               |
|-------------------------------|--------------------------------------------------------------------|
| Step the execution forward    | `LEFT MOUSE BUTTON`, `SPACE`, `RIGHT ARROW` or `SCROLL WHEEL DOWN` |
| Step the execution backward   | `LEFT ARROW` or `SCROLL WHEEL UP`                                  |
| Move the grid view            | Drag click any mouse button                                        |
| Zoom in/out                   | `CTRL` + `SCROLL WHEEL`                                            |
| Reset the grid view           | `RIGHT MOUSE BUTTON`                                               |
| Reset the algorithm execution | `R`                                                                |
| Toggle drawing visited nodes  | `T`                                                                |
| Switch algorithms             | `1`, `2`, `3`, `4`, `5`                                            |
