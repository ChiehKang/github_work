angular.module('myApp').controller('UserCtrl', UserCtrl);

function UserCtrl($rootScope, $scope, $http) {	
	$scope.logout = function() {
		$http({
			method : 'GET',
			url : '/dream/user/logout',
			data : {}
		}).then(function successCallback(response) {
			
			console.log(response);
			$rootScope.isLogin = false;
		});

	}
};
