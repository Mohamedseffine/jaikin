# Chaikin’s Algorithm — Java Swing Interactive Demo
An interactive visualization of **Chaikin’s Corner-Cutting Algorithm**, implemented using Java Swing.  
This program lets you plot control points on a canvas and animates the smoothing process step-by-step.

---

## 📚 Overview

Chaikin’s Algorithm is a geometric technique used to generate smooth curves from a polygon.  
Given a set of control points, each iteration:

- “Cuts corners” by removing sharp angles  
- Generates new points by interpolating between original ones  
- Produces a curve that converges toward a **quadratic B-spline**

This project visually demonstrates how the curve evolves over multiple iterations.

---

## 🚀 Features

- Click to create control points  
- Press **Enter** to start or continue the curve-smoothing animation  
- Watch up to 7 smoothing steps, then auto-reset  
- Press **Delete** to clear the canvas  
- Press **Escape** to exit  
- Real-time visualization using Swing + custom drawing  

---

# 🔍 Deep Code Explanation

Below is a **detailed breakdown** of how the code works, with explanations of the animation logic, Swing rendering, event listeners, and Chaikin mathematics.

---

# 1. Class Structure

### `public class ChaikinAlgorithm extends JPanel implements KeyListener, MouseListener`

- `JPanel`: Used as the drawing canvas.
- `KeyListener`: Allows the program to react to Enter/Delete/Escape.
- `MouseListener`: Allows adding control points by clicking the mouse.

---

# 2. Data Fields

```java
private List<Point> controlPoints = new ArrayList<>();
private List<Point> currentPoints = new ArrayList<>();
```

- **controlPoints**: The points drawn by the user.  
- **currentPoints**: The points currently being smoothed and rendered.

```java
private int currentStep = 0;
private boolean animationRunning = false;
```

Tracks the number of Chaikin iterations and whether the animation is currently active.

```java
private javax.swing.Timer animationTimer;
private final int POINT_RADIUS = 5;
private final int ANIMATION_DELAY = 1000; 
```

- Swing's `Timer` triggers an event every second.  
- This creates a controlled animation loop.

---

# 3. Constructor

```java
addMouseListener(this);
addKeyListener(this);
setFocusable(true);
```

Necessary for receiving keyboard and mouse input.

```java
animationTimer = new javax.swing.Timer(ANIMATION_DELAY, e -> {
    if (animationRunning) nextAnimationStep();
});
```

The timer drives the animation by repeatedly calling `nextAnimationStep()`.

---

# 4. Rendering (paintComponent)

Rendering is performed using Java2D:

```java
Graphics2D g2d = (Graphics2D) g;
g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
```

Enables visual smoothing.

---

## Drawing the Elements

### 4.1 Control Points (blue)

```java
g2d.setColor(Color.BLUE);
g2d.fillOval(...)
```

User-drawn points.

### 4.2 Smoothed Polyline (red)

```java
g2d.setColor(Color.RED);
g2d.drawLine(...)
```

Represents the smoothed result after each step.

### 4.3 Intermediate Generated Points (green)

Shows the Q1 and Q2 Chaikin points.

---

# 5. Animation Control

### 5.1 Starting the animation

```java
currentPoints = new ArrayList<>(controlPoints);
animationTimer.start();
```

Resets and begins the animation.

### 5.2 Performing a step

```java
currentPoints = chaikinStep(currentPoints);
currentStep++;
```

The smoothing iteration is applied.

If step >= 7, the animation resets.

---

# 6. The Algorithm (chaikinStep)

This is the heart of the project.

For each pair of points (P1, P2):

### Q1 (closer to P1)
```java
(3*p1 + p2) / 4
```

### Q2 (closer to P2)
```java
(p1 + 3*p2) / 4
```

These represent the 25% and 75% subdivision points.

---

# 7. Mouse Interaction

```java
controlPoints.add(e.getPoint());
currentPoints = new ArrayList<>(controlPoints);
```

Points can only be added when animation is not running.

---

# 8. Keyboard Interaction

| Key | Action |
|------|---------|
| **Enter** | Start animation |
| **Delete** | Clear all points |
| **Escape** | Exit |

---

# 9. Main Method

Uses Swing's EDT:

```java
SwingUtilities.invokeLater(() -> {
    JFrame frame = new JFrame("Chaikin's Algorithm Animation");
    frame.add(new ChaikinAlgorithm());
});
```

---

# 🧪 How the Algorithm Evolves

Each iteration:

- Doubles the number of points  
- Smoothens the curve  
- Removes corners  
- Converges toward a B-spline curve  

---

# 🖥️ Running the Program

Compile:

```bash
javac ChaikinAlgorithm.java
```

Run:

```bash
java ChaikinAlgorithm
```

---

# ✔️ Summary

This project showcases:

- Swing rendering  
- Repaint-driven animation  
- Geometry-based curve smoothing  
- Event-driven UI programming  
- Clean implementation of Chaikin’s Algorithm  

---

If you need a PDF version or want diagrams included, just ask!
