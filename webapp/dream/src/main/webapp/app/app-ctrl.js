angular.module('myApp').controller('AppCtrl', AppCtrl);

function AppCtrl($rootScope, $scope, $http) {
	$rootScope.isLogin = false;
	$scope.pageList = [
		{ url: '#!/home', name: 'Home' },
		{ url: '#!/pageOne', name: 'PageOne' },
		{ url: '#!/pageTwo', name: 'PageTwo' }];
};
