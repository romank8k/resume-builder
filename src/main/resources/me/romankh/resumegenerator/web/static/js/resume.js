// Use window load event due to calculations that require precise positioning (after the fonts load).
window.addEventListener('load', () => {
    const minHeight = 70;

    const $window = $(window);
    const $document = $(document);
    const $body = $('body');
    const $masthead = $('#resume-masthead');
    const $sidebar = $('#resume-sidebar');
    const $content = $('#resume-content');
    const $footer = $('#resume-footer');

    const viewportHeight = $window.height();
    const bodyHeight = $body.height();
    const contentHeight = $content.height();
    const footerHeight = $footer.outerHeight();
    const mastheadHeight = $masthead.outerHeight();  // Includes the border.
    const collapseBy = mastheadHeight - minHeight;  // Amount we'll be collapsing the masthead by.

    const maxBodyPaddingTop = parseInt($body.css('margin-top'), 10);
    const sidebarTop = parseInt($sidebar.css('top'), 10);
    const mastheadTotalMinHeight = minHeight + parseInt($masthead.css('border-bottom-width'), 10);

    // Extend the document body by as much space as is necessary
    // to scroll the last secondary resume element all the way to the top of the page.
    (() => {
        const documentHeight = $document.height() - collapseBy;
        const $lastSecondaryEl = $('.resume-secondary-section').last();
        if ($lastSecondaryEl.length) {
            const lastSecondaryYPosition = $lastSecondaryEl.position().top;
            const extendHeightBy = viewportHeight - (documentHeight - lastSecondaryYPosition) - footerHeight;
            if (extendHeightBy > 0) {
                $content.height(bodyHeight + extendHeightBy);
            }
        }
    })();

    // Control the collapsing/expanding animations.
    let isMastheadShown = true;
    let isTransitioning = false;
    const animationOptions = {
        duration: 200,
        easing: 'swing',
        queue: false
    };

    (() => {
        // Add extra pixels to align with the sidebar.
        const extra = parseInt($sidebar.css('margin-top'), 10);

        new bootstrap.ScrollSpy('#resume-content', {
            target: '#resume-sidebar',
            // Add 5 extra pixels to help active sidebar labels align correctly on FireFox.
            offset: mastheadTotalMinHeight + extra + 5,
            smoothScroll: true
        });
    })();

    const preventDefault = (e) => {
        e = e || window.event;
        if (e) {
            if (e.preventDefault) {
                e.preventDefault();
            }
            e.returnValue = false;
        }
    };

    const collapseMasthead = (e) => {
        if (isTransitioning) {
            preventDefault(e);
        }

        const currYScrollPos = $window.scrollTop();
        if (!isTransitioning && isMastheadShown && currYScrollPos >= 0) {
            isTransitioning = true;
            preventDefault(e);

            $.when(
                $masthead.animate({
                    'height': minHeight
                }, animationOptions), 
                $body.animate({
                    'margin-top': maxBodyPaddingTop - collapseBy
                }, animationOptions), 
                $sidebar.animate({
                    'top': sidebarTop - collapseBy
                }, animationOptions)
            ).done(() => {
                isTransitioning = false;
                isMastheadShown = false;

                var dataSpyList = [].slice.call(document.querySelectorAll('[data-bs-spy="scroll"]'))
                dataSpyList.forEach(function (dataSpyEl) {
                    bootstrap.ScrollSpy.getInstance(dataSpyEl)
                        .refresh()
                });
            });
        }
    };

    const expandMasthead = (e) => {
        const currYScrollPos = $window.scrollTop();
        if (!isTransitioning && !isMastheadShown && currYScrollPos <= 0) {
            isTransitioning = true;

            $.when(
                $masthead.animate({
                    'height': mastheadHeight
                }, animationOptions), 
                $body.animate({
                    'margin-top': maxBodyPaddingTop
                }, animationOptions), 
                $sidebar.animate({
                    'top': sidebarTop
                }, animationOptions)
            ).done(() => {
                isTransitioning = false;
                isMastheadShown = true;

                var dataSpyList = [].slice.call(document.querySelectorAll('[data-bs-spy="scroll"]'))
                dataSpyList.forEach(function (dataSpyEl) {
                    bootstrap.ScrollSpy.getInstance(dataSpyEl)
                        .refresh()
                });
            });
        }
    };

    // UP: 38, PAGEUP: 33, HOME: 36
    const scrollUpKeys = [38, 33, 36];
    // DOWN: 40, PAGEDOWN: 34, END: 35, SPACEBAR: 32
    const scrollDownKeys = [40, 34, 35, 32];

    let scrollIntervalTimerId = null;
    const keydown = (e) => {
        // The scroll 'down' keys are simple - we'll collapse the masthead since the page will scroll down.
        for (let i = scrollDownKeys.length; i--;) {
            if (e.keyCode === scrollDownKeys[i]) {
                collapseMasthead(e);
                return;
            }
        }

        // For the 'up' keys, we must make sure that the user has finished scrolling to the top of the page
        // and that the browser has finished rendering the scroll. This is what the `setInterval` is for, since there
        // isn't an event for this (that I know of).
        for (let i = scrollUpKeys.length; i--;) {
            if (e.keyCode === scrollUpKeys[i]) {
                let prevYScrollPos = $window.scrollTop();
                if (!scrollIntervalTimerId) {
                    scrollIntervalTimerId = setInterval(() => {
                        const currYScrollPos = $window.scrollTop();
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
    };

    const wheelHandler = (e) => {
        if (e) {
            if (e.deltaY && e.deltaY < 0) {
                expandMasthead(e);
            } else if (e.deltaY && e.deltaY > 0) {
                collapseMasthead(e);
            }
        }
    };

    const touchHandler = (e) => {
        if (e) {
            if ((e.deltaY && e.deltaY < 0) || e.offsetY) {
                expandMasthead(e);
            } else if (e && e.deltaY > 0) {
                collapseMasthead(e);
            } else {
                collapseMasthead(e);
            }
        }
    };

    const registerScrollListeners = () => {
        window.addEventListener('wheel', wheelHandler, false);
        window.addEventListener('touchmove', touchHandler, { passive: false });
        document.addEventListener('keydown', keydown);
        window.addEventListener('scroll', collapseMasthead);
    };

    // Call last.
    registerScrollListeners();

    // The masthead should be collapsed on initial page load if the scrollbar is not at the top.
    const currYScrollPos = $window.scrollTop();
    if (currYScrollPos > 0) {
        // Increase the animation duration temporarily.
        const animationDuration = animationOptions.duration;
        animationOptions.duration = 600;
        collapseMasthead();
        animationOptions.duration = animationDuration;
    }
});