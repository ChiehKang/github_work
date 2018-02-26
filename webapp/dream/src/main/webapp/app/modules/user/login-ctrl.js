angular.module('myApp').controller('LoginCtrl', LoginCtrl);

function LoginCtrl($rootScope, $scope, $http) {
	$scope.account = 'Jay';
	$scope.password = '1223';
	
	$scope.login = function() {
		$scope.errMsg = null;
		
		$http({
			method : 'POST',
			url : '/dream/user/login',
			data : {
				"account" : $scope.account,
				"password" : $scope.password,
			}
		}).then(function successCallback(response) {
			
			console.log(response);
			if(response.data.datas != null) {
				$rootScope.userName = response.data.datas.userName;
				$rootScope.isLogin = true;
			}
			else {
				$scope.errMsg = response.data.errMsg;
			}
		});
	}
};
