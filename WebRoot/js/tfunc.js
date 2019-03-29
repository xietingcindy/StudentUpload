


//点赞
  
  	function praise(id){
				var id=id;
				//alert(id);
				//console.log("id:"+id);
			    $(function(){
			            $.ajax({
			                async : true,
			                //https://api.douban.com/v2/book/17604305?fields=
			                url : "StudentServlet",
			                type : "GET",
			                dataType : "json", // 返回的数据类型，设置为JSONP方式
			                /*jsonp : 'callback', //指定一个查询参数名称来覆盖默认的 jsonp 回调参数名 callback
			                jsonpCallback: 'handleResponse', //设置回调函数名*/
			               	data:{
			               		wid:id,
			               		method:"praise"
			               	
			               	},
			               
			                success: function(result){
			                		
			                			alert("点赞成功！");
				                    	console.log("id:"+id);	
			                	
			                    			                
			                   
			                }
			            	
			            });
			    })
			   };
	   
			   
			   
			   
			   
			   
			   
			   

//通过审核
  
  	function pass(id){
				var id=id;
			    $(function(){
			            $.ajax({
			                async : true,
			                //https://api.douban.com/v2/book/17604305?fields=			                
			                url : "TeacherServlet",
			                type : "get",
			                dataType : "json", // 返回的数据类型，设置为JSONP方式
			                
			               	data:{
			               		wid:id,
			               		method:"approved"
			               		
			               	
			               	},
			                success: function(result){
			                    if(result[0].result=='success'){
			                    	alert("审核成功！");
			                    	window.location.reload();
			                    	console.log("wid:"+id);
			                   }
			                   else{
			                   	 alert("审核失败！");
			                   }
			                  
			                   console.log(result);
			                   
			                },
			                error: function() {
								alert("出错啦!");
							}
			            });
			    })
			   };
	//删除
			   
	function deletework(id){
			var id=id;			   
			if(confirm("您确定要删除吗？")){
	
			    $(function(){
			            $.ajax({
			                async : true,
			                //https://api.douban.com/v2/book/17604305?fields=
			                url : "TeacherServlet",
			                type : "post",
			                dataType : "json", // 返回的数据类型，设置为JSONP方式
			                
			               	data:{
			               		wid:id,
			               		method:"delete"
			               	
			               	},
			                success: function(result){
			                	   if(result[0].result=='success'){
			                		   alert("删除成功！");
				                    	console.log("wid:"+id);
				                   }
				                   else{
				                   	 alert("删除失败！");
				                   }
			                
			                   
			                   window.location.reload();
			                },
			               	error: function() {
			               		alert("出错啦!");
							}
			            });
			    })
			   }
			else{
				return;
			}

}

  		   

			
//筛选已审核
	
	
	

		function getPassData(page) {
			var page = page;
			$(function() {
				$.ajax({
					type: "get",
					//url : "DwonServlet",
					url: "TeacherServlet", //请求的url地址
					dataType: "json", //返回格式为json,也可以不添加这个属性，也可以是（xml、json、script 或 html）。
					async: false, //请求是否异步，默认为异步，这也是ajax重要特性
					data: {						
						page: page,
						limit: 9,
						select:"selectPass",
						method:"select"
						//method:"findByPass"
					}, //参数值 ,id是定义的要传的参数变量名，后台接收一致，value是页面取的传值的变量名，如果有多个，按照格式每组用逗号隔开，没有参数可以不用
			
					success: function(doc) {
						
					
						renderPassData(doc);
					
					},
					error: function() {
						alert("出错了，没取到啊!");
					}
				});
			});
		};

//筛选未审核

		function getUnpassData(page) {
			var page = page;
			$(function() {
				$.ajax({
					type: "get", //提交方式，也可以是get
					url : "TeacherServlet",
					dataType: "json", //返回格式为json,也可以不添加这个属性，也可以是（xml、json、script 或 html）。
					async: false, //请求是否异步，默认为异步，这也是ajax重要特性
					data: {
						method:"select",
						select:"selectUnPass",
						page: page,
						limit: 9,
						//method:"findByaUnPass"
					}, //参数值 ,id是定义的要传的参数变量名，后台接收一致，value是页面取的传值的变量名，如果有多个，按照格式每组用逗号隔开，没有参数可以不用
			
					success: function(doc) {
						//var data=JSON.parse(doc);
					
						renderUnPassData(doc);
						//console.log(data);
					},
					error: function() {
						alert("出错了，没取到啊!");
					}
				});
			});
		};
		
		//显示未审核作品
		function renderUnPassData(doc) {
			var str = '';
			doc.forEach(function(dataDoc) {
				str +=  `<div class="col-md-4">
							<div class="thumbnail">
								<a href="studentSingle.html?wid=${dataDoc.wid}">
									<img style="width:255px;height:163px" onerror="this.src='img/error.png'" alt="暂无图片，已显示默认图片" src="${dataDoc.picture}" />
								</a>
								<div class="caption">
									<h3>
										${dataDoc.wname}
									</h3>
									<p class="intro">
										${dataDoc.introduce}
									</p>
									<p>
										 <a class="btn" name="praise" href="javascript:void(0)" onclick="pass(${dataDoc.wid})"><i class="layui-icon layui-icon-edit">通过</i></a> 
										 <a class="btn" name="warm" href="javascript:void(0)" onclick="deletework(${dataDoc.wid})"><i class="layui-icon layui-icon-delete">删除</i></a>
										 <a class="btn" name="upload" href="${dataDoc.address}" download="${dataDoc.address}"><i class="layui-icon layui-icon-download-circle">下载</i></a> 
									</p>
								</div>	
							</div>
						</div>
		`;

			});
			document.querySelector("#row").innerHTML = str;
		};
		//显示筛选审核作品	
		function renderPassData(doc) {
			var str = '';
			doc.forEach(function(dataDoc) {
				str +=  `<div class="col-md-4">
							<div class="thumbnail">
								<a href="studentSingle.html?wid=${dataDoc.wid}">
									<img style="width:255px;height:163px" onerror="this.src='img/error.png'" alt="暂无图片，已显示默认图片" src="${dataDoc.picture}" />
								</a>
								<div class="caption">
									<h3>
										${dataDoc.wname}
									</h3>
									<p class="intro">
										${dataDoc.introduce}
									</p>
									<p>
										 <a class="btn" name="warm" href="javascript:void(0)" onclick="deletework(${dataDoc.wid})"><i class="layui-icon layui-icon-delete">删除</i></a>
										 <a class="btn" name="upload" href="${dataDoc.address}" download="${dataDoc.address}"><i class="layui-icon layui-icon-download-circle">下载</i></a> 
									</p>
								</div>	
							</div>
						</div>
		`;

			});
			document.querySelector("#row").innerHTML = str;
		};
		
		