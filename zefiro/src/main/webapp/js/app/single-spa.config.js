


var zefiroApp = window.singleSpaAngularjs.default({
  angular: window.angular,
  domElementGetter: function () {
	  return document.getElementById('zefiro-app')
  },
  mainAngularModule: 'main',
  uiRouter: false,
  preserveGlobal: true,
})

window.singleSpa.registerApplication('zefiro-app', zefiroApp, function activityFunction(location) {
	
	console.log(location);
  return true;
  })

window.singleSpa.start();