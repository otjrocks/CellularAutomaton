# Guide: Create A New Theme

* To create a new theme with the name `Example`, create a new file named `Example.css` in this
  directory.
* The information in this file will be directly applied to the scene whenever the new theme is
  selected.
* By default, there are multiple CSS variables that you should set to ensure that the theme has
  correct color values. Copy the below code as a boilerplate to create your new theme. Update the
  CSS variables with your desired color values. Additionally, you can add more custom css to your
  theme file and it will be applied.
* For example implementation of themes, view the various default themes.

## Required theme CSS variables:

```
* {
  -fx-primary: #1D1616;
  -fx-secondary: #EEEEEE;
  -fx-light: #232121;
  -fx-accent: #D95F59;
  -fx-warning: #D84040;
}
```