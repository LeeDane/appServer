$(document).ready(function(){
	 $('.panel-collapse').on('show.bs.collapse', function () {
		 $(this).siblings(".panel-heading").find("span").hide();		 
		 $(this).parent().siblings(".panel").find("span").show();
       });
	 $('.panel-collapse').on('hidden.bs.collapse', function () {
		 $(this).siblings(".panel-heading").find("span").show();		 
       });
});
