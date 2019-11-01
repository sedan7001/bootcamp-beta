app.config(function($stateProvider, $urlRouterProvider) {
 
});
 
app.run(function($translatePartialLoader) {
	$translatePartialLoader.addPart('/apps/test4/test4/locales');
});
 
//# sourceURL=apps/test4/test4/config.js