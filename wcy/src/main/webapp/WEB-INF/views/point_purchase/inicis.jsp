<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript" src="https://cdn.iamport.kr/js/iamport.payment-1.1.5.js"></script>
</head>


<body>
    <script>
    $(function(){
        var IMP = window.IMP; // 생략가능
        IMP.init('imp26991037'); 
        var msg;
        
        IMP.request_pay({
            pg : 'inicis',
            pay_method : 'card',
            merchant_uid : 'merchant_' + new Date().getTime(),
            name : '${point} '+ '포인트',
            amount : '${amount}',
            buyer_email : '${login.userEmail}',
            buyer_name : '${login.userName}',
            buyer_tel : '${login.userTel}',
            buyer_postcode : '${login.userZipcode}',
        }, function(rsp) {
            if ( rsp.success ) {
            	
            	
                //[1] 서버단에서 결제정보 조회를 위해 jQuery ajax로 imp_uid 전달하기
                $.ajax({
                    url: "/pay/paying", 
                    type: 'POST',
    				headers:{
    					"Content-Type": "application/json"
    				},
                    dataType: 'text',
                    data:JSON.stringify({
                    		pointPurchaseId : rsp.imp_uid,
                       		pointPurchaseUserNo : '${login.userNo}',
                    		pointPurchasePurchasedAt : new Date().getTime(),
                       		pointPurchaseAmount : rsp.paid_amount
							
                        	//기타 필요한 데이터가 있으면 추가 전달
							}),success : function(result){
									console.log("ajax 통신 성공");
									console.log(result);
								
							},error : function(){console.log("실패")}
								
						
                }).done(function(data) {  			
       	          alert("결제 성공");

                	opener.document.location.reload();
               		self.close();})
                    //[2] 서버에서 REST API로 결제정보확인 및 서비스루틴이 정상적인 경우

               /* location.href="/pay/paycomplete"; */
                //성공시 이동할 페이지
				
                
          	  } else {
                msg = '결제에 실패하였습니다.';
                msg += ' 에러내용 : ' + rsp.error_msg;
                alert(msg);
   	            console.log("결제 실패");

 				opener.document.location.reload();
                self.close();
            }
        });
        
    });
    </script>
 
</body>
</html>
