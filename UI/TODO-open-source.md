#Thoughts/recommendations for enhancements before open-sourcing
---

1. Create a global header instead of making it part of the template
    - Using events could allow the dashboard controller to dynamically set pieces like the title

2. Consider packaging templates and themes
    - Move themes to subfolders so they can contain their own javascript, fonts, images, etc without adding those files to core.
    - It may be logical to group templates with themes and have a javascript file which registers any templates/themes associated with it.
    - These folders could contain a javascript file to register the theme and template so new ones don't need to be added manually.

3. Implement operative threading
    - Processing data on a background thread is a good practice and the operative library makes it easy to use web workers
    - Web workers require passing in urls for libraries to be used (such as lodash), but these are part of the compiled javascript and we don't want to pass it all in
    - Update the build to copy useful frameworks such as lodash and moment to a scripts folder as-is so they can be referenced when using operative

4. Add text-management classes
    - To keep fonts consistent widgets the framework uses mixins such as 'text-standard'. As use cases become more apparent it may be smart to move these to css classes that can be appended to dom elements with values controlled through less variables.

5. Remove bower_components from GIT
    - bower_components folder was added to github to aid the build process at Capital One, but should be removed before open sourcing.
    
6. Remove local testing
    - The localTesting variable, used throughout various widgets and data factories was only to enable development until a development API could be stood up. 
    - Once a development API is available all references to localTesting and the test-data directory should be removed.

7. Implement widget-placeholder directive
    - Widget placeholder directives could control the interface for dynamic addition of widgets. This would take API work as well.

8. More responsive layouts
    - Instead of designing widgets with multiple sections, separate each as its own widget.
    To prevent configuring each widget individually, consider using the widget-container directive to control updating them all at once. 
    - Widgets and layouts are technically already responsive using Bootstrap's grid system. 
    However, this does not take into consideration the containing column a widget may be in. A new 


    <div class="col-md-6"> <!-- from template file, but what if it was col-md-3? -->
        <widget name="build">
            <div class="build-view"> <!-- from widget's view.html -->
                <div class="row">
                    <div class="col-lg-8 dash-min-col-3"> <!-- a new grid class -->
                        <builds-per-day></builds-per-day>
                        <average-build-duration></average-build-duration>
                    </div>
                    <div class="col-lg-4">
                        <div class="row">
                            <div class="col-lg-12 col-sm-6">
                                <latest-builds></latest-builds>
                            </div>
                            <div class="col-lg-12 col-sm-6">
                                <total-builds></total-builds>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </widget>
    </div>


 The code above will respond as designed, but the widget has been build optimized for a 50% parent column for the MVP. 
 If this were moved to a smaller column, say 25% (col-md-3), it would respond as designed, but not look good. 
 
 A recommendation for improving responsive designs is to create a new set of column classes to define the minimum
 target width and override Bootstrap's column classes to be a width 100% if it falls below that mark.
 At the time of this project I am not aware of any libraries which do this. 
    
An example of untested code that could do something like this and would need to be expanded for other boostrap column sizes:

    .col-md-6 .col-md-8.dash-min-col-3 {
        // no change, it's requesting a minimum of 25% of the screen. 
        // At 75% inside a 50% it is still above the 25% threshold.
    }
    
    .col-md-3 .col-md-8.dash-min-col-3 {
        // we are now 75% of a 25% grid which is below the threshold and means it
        // should be resized to 100% and override the normal bootstrap styles.
        width: 100%;
    }
    
This would state that the element is a 50% grid, but at minimum it must be 33% of the screen. If the grid goes below that 
threshold it would become 100%. These could also potentially be simplified with media queries to better support
Bootstrap's xs/sm/md/lg classes.