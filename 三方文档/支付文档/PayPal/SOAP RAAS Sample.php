
        <?php 
		//phpinfo();
		
		try
		{
			//WSDL
			$apta_server = 'https://svcs.sandbox.paypal.com/RiskAssessment/SetTransactionContext?wsdl';
			$options = array('trace' => 1, 'exceptions' => 0);
			//EndPoint
			$options['location'] = 'https://svcs.sandbox.paypal.com/RiskAssessment/SetTransactionContext';
			//API Credential
            $stream_opts = array('http'=>array('header'=>"X-PAYPAL-SECURITY-PASSWORD: 1357486538\r\n"."X-PAYPAL-SECURITY-SIGNATURE: AFcWxV21C7fd0v3bYYYRCpSSRl31AlKtnA4bVAMN.yOW0Ni4YwEi19aq\r\n".
                                               "X-PAYPAL-SECURITY-USERID: seller_1357486518_biz_api1.yahoo.com\r\n"));
            
			$options['stream_context'] = stream_context_create($stream_opts);
			//Post Data
			$post_data= array(
				'pt:SetTransactionContextRequest'=>array(
					'requestEnvelope'=>array('errorLanguage'=>'en_US'), 
					'trackingId'=>'EC-6Y271971AK199532U'));
			            
			
			try
			{				
				$client = new SoapClient($apta_server, $options);
			}
			catch (Exception $e)
			{
				
			}
			try
			{
				//SOAP Call
				$result = $client->__soapCall('SetTransactionContext',$post_data);
				echo '<h1>Soap Result</h1><pre>';
				print_r($result);
				echo '</pre>';
			}
			catch (SoapFault $fault)
			{
				echo '<h1>Soap Fault</h1><pre>';
				print_r($fault);
				echo '</pre>';
			}
			
		}
		catch (Exception $e)
		{
			print_r('\nError Message:'.$e->getMessage());
		}
		
		?>