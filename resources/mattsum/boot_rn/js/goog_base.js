/* Overrides how Google Closure's provide and require functions work, in order for them to work with React Native's packager.
 */
if (typeof global !== 'undefined') {

    if (goog.LOCALE === undefined) {
        goog.LOCALE = "en";
    }

    //TODO: this should probably not be in here
    //React Native throws error because it does not support group logging
    console.groupCollapsed = console.groupCollapsed || console.log;
    console.group = console.group || console.log;
    console.groupEnd = console.groupEnd || function() {};

    goog.provide = function(name) {
        if (goog.isProvided_(name)) {
            return; //don't throw an error when called multiple times, because it is going to be called multiple times in from react-native
        }
        //Rest of the code is copied as is from Closure
        delete goog.implicitNamespaces_[name];

        var namespace = name;
        while ((namespace = namespace.substring(0, namespace.lastIndexOf('.')))) {
            if (goog.getObjectByName(namespace)) {
                break;
            }
            goog.implicitNamespaces_[namespace] = true;
        }
        goog.exportPath_(name);
    };

    var orig_require = goog.require;
    goog.require = function(moduleId) {
        if ( typeof moduleId === 'number' ) {
            // If a numberic moduleId is passed, the module was transformed by
            // the RN packager, replacing the string with a unique id. In this
            // case we want to require the id using the RN packager.

            require(moduleId);
        }
        else {
            console.log("goog.require: " + moduleId);
            orig_require(moduleId);
        }
    };
}

// Overrides environment to make other tools that expect browser environment work
function fakeLocalStorageAndDocument() {
    window.localStorage = {};
    window.localStorage.getItem = function() { console.warn('localStorage is faked - see goog/base.js');
                                               return 'true'; };
    window.localStorage.setItem = function() { console.warn('localStorage is faked - see goog/base.js'); };

    window.document = {};
    window.document.body = {};
    window.document.body.dispatchEvent = function() { console.warn('dispatchEvent is faked - see goog/base.js'); };
    window.document.createElement = function() {  console.warn('createElement is faked - see goog/base.js'); };

    if (typeof window.location === 'undefined') {
        console.log('Shimming window.location');
        window.location = {};
        window.location.href = "http://example.org";
        window.location.protocol = "http"; //for boot-reload "connect" event
    } else {
        console.warn('Not shimming window.location - location already set to ' + window.location.href);
    }
    window.addEventListener = function() {};
}
fakeLocalStorageAndDocument();


