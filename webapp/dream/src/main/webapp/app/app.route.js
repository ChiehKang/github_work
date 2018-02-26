angular.module('app.route', []).config(function($stateProvider, $urlRouterProvider)  {
			$urlRouterProvider.otherwise("/home");
			$stateProvider.state('home', {
				url: '/home',
				templateUrl : 'app/modules/home/home.html',
				controller : 'HomeCtrl',

			}).state('pageOne', {
				url: '/pageOne',
				templateUrl : 'app/modules/pageOne/pageOne.html',
				controller : 'PageOneCtrl',
				
			}).state('pageTwo', {
				url: '/pageTwo',
				templateUrl : 'app/modules/pageTwo/pageTwo.html',
				controller : 'PageTwoCtrl',
				
			})
		});