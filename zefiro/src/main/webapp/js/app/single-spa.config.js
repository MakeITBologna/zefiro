


console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
var zefiroApp = window.singleSpaAngularjs.default({
  angular: window.angular,
  domElementGetter: function () {
	  
	  console.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	  
    // Note that we will need to add a div with this id to our index.html, we will do this in step four
    return document.getElementById('zefiro-app')
  },
  mainAngularModule: 'main',
  uiRouter: false,
  preserveGlobal: true,
  // We will be building this template in step four
  template: '<zefiro-app />',
})
console.log(zefiroApp);

window.singleSpa.registerApplication('zefiro-app', zefiroApp, function activityFunction(location) {
  return true;
  })

window.singleSpa.start();