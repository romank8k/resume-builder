// Use window.onload due to calculations that require precise positioning (after the fonts load).
$(window).load(function () {
    var minHeight = 70;

    var $window = $(window);
    var $document = $(document);
    var $body = $('body');
    var $masthead = $('#resume-masthead');
    var $sidebar = $('#resume-sidebar');
    var $content = $('#resume-content');
    var $footer = $('#resume-footer');

    var viewportHeight = $window.height();
    var bodyHeight = $body.height();
    var contentHeight = $content.height();
    var footerHeight = $footer.outerHeight();
    var mastheadHeight = $masthead.outerHeight();  // Includes the border.
    var collapseBy = mastheadHeight - minHeight;  // Amount we'll be collapsing the masthead by.

    var maxBodyPaddingTop = parseInt($body.css('margin-top'), 10);
    var sidebarTop = parseInt($sidebar.css('top'), 10);
    var mastheadTotalMinHeight = minHeight + parseInt($masthead.css('border-bottom-width'), 10);

    (function() {
        // Extend the document body by as much space as is necessary
        // to scroll the last secondary resume element all the way to the top of the page.
        var documentHeight = $document.height() - collapseBy;
        var $lastSecondaryEl = $('.resume-secondary-section').last();
        if ($lastSecondaryEl.length) {
            var lastSecondaryYPosition = $lastSecondaryEl.position().top;
            var extendHeightBy = viewportHeight - (documentHeight - lastSecondaryYPosition) - footerHeight;
            if (extendHeightBy > 0) {
                $content.height(bodyHeight + extendHeightBy);
            }
        }
    })();

    // Control the collapsing/expanding animations.
    var isMastheadShown = true;
    var isTransitioning = false;
    var animationOptions = {
        duration: 200,
        easing: 'swing',
        queue: false
    };

    (function() {
        // Add extra pixels to align with the sidebar.
        var extra = parseInt($sidebar.css('margin-top'), 10);

        $body.scrollspy({
            target: '#resume-sidebar',
            // Add 5 extra pixels to help active sidebar labels align correctly on FireFox.
            offset: mastheadTotalMinHeight + extra + 5
        });

        $sidebar.localScroll({
            duration: 800,
            hash: false,
            easing: 'swing',
            onBefore: function (e, anchor, $target) {
            },
            onAfter:function ($anchor, settings) {
            },
            offset: function () {
                var offset = isMastheadShown ? -(minHeight + collapseBy + extra) : -(minHeight + extra);
                return {
                    top: offset
                }
            }
        });
    })();

    function collapseMasthead (e) {
        if (isTransitioning)
            preventDefault(e);

        var currYScrollPos = $window.scrollTop();
        if (!isTransitioning && isMastheadShown && currYScrollPos >= 0) {
            isTransitioning = true;
            preventDefault(e);

            $.when($masthead.animate({
                'height': minHeight
            }, animationOptions), $body.animate({
                'margin-top': maxBodyPaddingTop - collapseBy
            }, animationOptions), $sidebar.animate({
                'top': sidebarTop - collapseBy
            }, animationOptions)).done(function () {
                isTransitioning = false;
                isMastheadShown = false;
            });
        }
    }

    function expandMasthead (e) {
        var currYScrollPos = $window.scrollTop();
        if (!isTransitioning && !isMastheadShown && currYScrollPos <= 0) {
            isTransitioning = true;

            $.when($masthead.animate({
                'height': mastheadHeight
            }, animationOptions), $body.animate({
                'margin-top': maxBodyPaddingTop
            }, animationOptions), $sidebar.animate({
                'top': sidebarTop
            }, animationOptions)).done(function () {
                isTransitioning = false;
                isMastheadShown = true;
            });
        }
    }

    // UP: 38, PAGEUP: 33, HOME: 36
    var scrollUpKeys = [38, 33, 36];
    // DOWN: 40, PAGEDOWN: 34, END: 35, SPACEBAR: 32
    var scrollDownKeys = [40, 34, 35, 32];

    function preventDefault (e) {
        e = e || window.event;
        if (e) {
            if (e.preventDefault)
                e.preventDefault();
            e.returnValue = false;
        }
    }

    var scrollIntervalTimerId = null;
    function keydown (e) {
        // The scroll 'down' keys are simple - we'll collapse the masthead since the page will scroll down.
        for (var i = scrollDownKeys.length; i--;) {
            if (e.keyCode === scrollDownKeys[i]) {
                collapseMasthead(e);
                return;
            }
        }

        // For the 'up' keys, we must make sure that the user has finished scrolling to the top of the page
        // and that the browser has finished rendering the scroll. This is what the `setInterval` is for, since there
        // isn't an event for this (that I know of).
        for (var i = scrollUpKeys.length; i--;) {
            if (e.keyCode === scrollUpKeys[i]) {
                var prevYScrollPos = $window.scrollTop();
                if (!scrollIntervalTimerId) {
                    scrollIntervalTimerId = setInterval(function () {
                        var currYScrollPos = $window.scrollTop();
                        if (currYScrollPos < prevYScrollPos) {
                            // We're still animating the scroll.
                            // Check whether we're done in the next interval.
                            prevYScrollPos = currYScrollPos;
                        } else {
                            // We stopped animating the scroll, check whether we should expand the masthead.
                            clearInterval(scrollIntervalTimerId);
                            scrollIntervalTimerId = null;
                            expandMasthead(e);
                        }
                    }, 75);
                }
                return;
            }
        }
    }

    function wheelHandler (e) {
        if (e) {
            if (e.deltaY && e.deltaY < 0) {
                expandMasthead(e);
            } else if (e.deltaY && e.deltaY > 0) {
                collapseMasthead(e);
            } else {
                //collapseMasthead(e);
            }
        }
    }

    function touchHandler(e) {
        if (e) {
            if ((e.deltaY && e.deltaY < 0) || e.offsetY) {
                expandMasthead(e);
            } else if (e && e.deltaY > 0) {
                collapseMasthead(e);
            } else {
                collapseMasthead(e);
            }
        }
    }

    function registerScrollListeners () {
        window.addWheelListener(window, wheelHandler);

        if (window.addEventListener) {
            // iOS
            window.addEventListener("touchmove", touchHandler, false);
        }
        document.onkeydown = keydown;

        $(window).scroll(function (e) {
            collapseMasthead(e);
        });
    }

    // From https://developer.mozilla.org/en-US/docs/Web/Reference/Events/wheel
    var prefix = "", _addEventListener, onwheel, support;

    // Detect event model.
    if ( window.addEventListener ) {
        _addEventListener = "addEventListener";
    } else {
        _addEventListener = "attachEvent";
        prefix = "on";
    }

    // Detect available wheel event.
    support = "onwheel" in document.createElement("div") ? "wheel" :  // Modern browsers support "wheel"
            document.onmousewheel !== undefined ? "mousewheel" :  // Webkit and IE support at least "mousewheel"
        "DOMMouseScroll";  // let's assume that remaining browsers are older Firefox

    window.addWheelListener = function (elem, callback, useCapture) {
        _addWheelListener( elem, support, callback, useCapture );

        // Handle MozMousePixelScroll in older Firefox.
        if (support == "DOMMouseScroll") {
            _addWheelListener( elem, "MozMousePixelScroll", callback, useCapture );
        }
    };

    function _addWheelListener(elem, eventName, callback, useCapture) {
        elem[_addEventListener](prefix + eventName, support == "wheel" ? callback : function(originalEvent) {
            !originalEvent && (originalEvent = window.event);

            // Create a normalized event object.
            var event = {
                // Keep a ref to the original event object.
                originalEvent: originalEvent,
                target: originalEvent.target || originalEvent.srcElement,
                type: "wheel",
                deltaMode: originalEvent.type == "MozMousePixelScroll" ? 0 : 1,
                deltaX: 0,
                delatZ: 0,
                preventDefault: function () {
                    originalEvent.preventDefault ?
                        originalEvent.preventDefault() :
                        originalEvent.returnValue = false;
                }
            };

            // Calculate deltaY (and deltaX) according to the event.
            if (support == "mousewheel") {
                event.deltaY = - 1/40 * originalEvent.wheelDelta;
                // Webkit also support wheelDeltaX
                originalEvent.wheelDeltaX && (event.deltaX = - 1/40 * originalEvent.wheelDeltaX);
            } else {
                event.deltaY = originalEvent.detail;
            }

            // It's time to fire the callback.
            return callback(event);
        }, useCapture || false);
    }

    // Call last.
    registerScrollListeners();

    // The masthead should be collapsed on initial page load if the scrollbar is not at the top.
    var currYScrollPos = $window.scrollTop();
    if (currYScrollPos > 0) {
        // Increase the animation duration temporarily.
        var animationDuration = animationOptions.duration;
        animationOptions.duration = 600;
        collapseMasthead();
        animationOptions.duration = animationDuration;
    }
});