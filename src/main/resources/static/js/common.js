(function(){
    function notify(){
      try { parent.postMessage({ type: 'iframe-url', href: location.href }, '*'); } catch(e) {}
    }
    ['pushState','replaceState'].forEach(function(fn){
      var orig = history[fn];
      history[fn] = function(){ var r = orig.apply(this, arguments); notify(); return r; };
    });
    window.addEventListener('popstate', notify);
    window.addEventListener('hashchange', notify);
    window.addEventListener('load', notify);
    setTimeout(notify, 0);
  })();