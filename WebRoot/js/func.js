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
	//踩		   
  	function warm(id){
				var id=id;
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
			               		method:"stamp"
			               	
			               	},
			                success: function(doc){
			                    
			                   alert("踩成功！");
			                   
			                }
			            });
			    })
			   };
			   
	