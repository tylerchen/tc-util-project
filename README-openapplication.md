open-application
================
<server>
	<containers>
		<container name processor>
<!-- 
container: the same as groovy classloader, a TCCLassManager instance will be treat as a container.
name: if name is not specify, default value is "default". 
-->
			<resources>
				<resource name type processor/>
<!-- 
resource: define public resource for all application.
-->
			</resources>
			<applications>
				<application name type processor>
					<settings>
						<setting name type processor/>
					</settings>
					<modules>
						<module name type processor>
							<actions>
								<action name type processor/>
							</actions>
						</module>
					</modules>
				</application>
			</applications>
		</container>
	</containers>
	<definitions>
		<definition name>
			
		</definition>
	</definitions>
</server>