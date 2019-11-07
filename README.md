# bootcamp-2019

네 자그럼 ui개발 파트를 진행해 보겠습니다. 스템0클릭

## step 0. parser setting

먼저 스텝0부분은 ui개발 환경을 갖추는 부분인데요 서버측 코드까지는 세팅된 상태로 인스턴스를 제공하기로 해서 지금은 바로 다음단계로 넘어가겠습니다. 

혹시 제로상태부터 내가 다 세팅하고 싶다! 이러신분들은 1번부터 20번까지 따라하시면 직접세팅 하실 수 있겠습니다.


## 배우는 것들
- 애플리케이션 생성, 빌드, 로그프레소에 추가
- 앵귤러 8.0
- 시나리오 기반 데이터 연동
- step1 ~ step11 git branch
https://github.com/logpresso/bootcamp-2019
	
	여기서부터는 참가자 여러분들이 직접 타이핑을 하시면서 실습을 하시게 될 건데요 여러분들이 직접 앱을 생성하고 빌드해서 로그프레소에 추가하는 부분까지 진행해 보겠습니다.
	
	애플리케이션 개발에 사용할 앵귤러 프레임워크 버전은 가장 최신인 8.0 을 사용하겠습니다. 앵귤러를 경험해보지 않으신 분들도 있을텐데요

	앱을 개발하실때 조금이라도 빠르고 쉽게 접근하실수 있도록 핵심개념 몇가지로 준비해 봤구요. 귀한 시간 내서 와주셨는데~
	
	앵귤러로 앱을 만드시는데 조금이라도 도움이 될수 있도록 노력해보겠습니다.
	
	뒷부분에는 튜토리얼 식으로 조그만 앱을 만들어 볼건데요 이번 부트캠프 시나리오 기반 데이터를 앱에 연동해서 보여주는 부분까지 진행을 하게 되겠습니다.

	이번 실습에 필요한 코드들은 모두 깃헙 리파지토리에 준비되어 있구요 step1번 브랜치부터 step11번 브랜치까지 올라가 있습니다. 중간에 하시다가 막힌다거나 하시면 해당 브랜치를  사용하시면 되겠습니다.
	
	그럼 이제 터미널을 열어주시고요 스텝1번부터 타이핑 해보겠습니다.


## step 1. createAppProject

- logpresso.createAppProject
	```
	telnet localhost 7008
	Trying ::1...
	telnet: connect to address ::1: Connection refused
	Trying 127.0.0.1...
	Connected to localhost.
	Escape character is '^]'.
	login as: root
	password:

	Please change the default password.
	New password:
	Retype password:
	Password changed successfully.

	Logpresso 3.9.1.1 (build 20191029) on Araqne Core 3.4.5
	``` 

	여기서 입력한 부분들은 다음 단계에서 살펴보겠습니다.

	```
	logpresso.createAppProject
	Project path? /Users/mac/Documents/bootcamp-2019/bootcamp-app
	Bundle Symbolic Name? com.logpresso.bootcamp
	Bundle Version? 1.0
	App ID? bootcamp
	App Name? bootcamp
	App Version? 1.0
	Required Version (empty line to default version 3.0)?4.0
	Program ID (empty line to exit)? bootcamp
	Program Display Name? bootcamp
	Program Profiles? all,admin,member
	Program ID (empty line to exit)?
	```

## 2. manifest.json
- manifest.json

	실제로 앱을 추가해보면 주로 경로설정 문제를 많이 겪을건데요 뭐 페이지가 안열린다던지 404 에러가 뜬다던지 하는 경우가 있죠. 
	
	이 파일만 제대로 설정을 해주시면 아마 앱을 추가하는게 어렵진 않을거에요
	
	스텝1에서 createAppProject를 하면 bootcamp-app 폴더에 자동으로 생성이 되었을 건데요. 해당 경로로 복사해주시면 되구요.
	
	>`/bootcamp-2019/bootcamp-app/src/main/resources/manifest.json`

	```
	{
		"required_version": "4.0",
		"app_version": "1.0.0",
		"bundle_symbolic_name": "com.logpresso.bootcamp",
		"app_names": {
			"ko": "bootcamp",
			"en": "bootcamp"
		},
		"programs": [
			{
				"program_names": {       
					"ko": "program_names", 
					"en": "program_names"
				},
				"program_id": "bootcamp", 
				"program_profiles": [
					"all",
					"admin",
					"member"
				]
			}
		],
		"app_id": "app_id", 
		"bundle_version": "1.0.0"
	}
	```

## 3. Angular-cli 로 프로젝트 생성

- Angular-cli

	앵귤러 cli는 프로젝트 생성, 템플릿 자동생성, 개발 서버, 테스트 등등 다양한 기능을 지원하기 때문에 앵귤러 프로젝트를 진행하는데는 필수 개발도구라고 보셔야되요.

	앵귤러로 개발하실때는 꼭 설치를 해주시면 되겠습니다. 

	```
	yarn global add @angular/cli
	```
	```
	? Would you like to add Angular routing? Yes
	? Which stylesheet format would you like to use? Less
	```
- ng 명령어

	앵귤러 cli 설치가 완료되면 이제 ng 명령어를 사용할 수 있는데요 ng버전, 그리고 ng뉴부트캠프 까지 실행을 해 주세요.
	```
	ng --version
	```

	<img src="./image/angular-cli.png">

- ng new

	앵귤러 cli의 프로젝트 생성 명령은 `ng new` 입니다. 이를 이용해 프로젝트를 생성해 보겠습니다. 이 경로로 들어가셔서 ng new bootcamp
	>`/bootcamp-2019/bootcamp-app/src/main/`
	```
	ng new bootcamp
	```

## 4. outputPath, base href 경로 수정
- angular.json
	
	angular.json이랑 index.html 파일을 수정해 주세요
	여기 outputPath는 step2에서 지정한 program_id랑 일치시켜줘야 되는 부분이구요.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/angular.json`
	```
	"outputPath": "../resources/WEB-INF/bootcamp",
	```

- index.html	
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/index.html`
	```
	<base href="./">
	```


## 5. 빌드, 로그프레소 대메뉴에 추가하기
- ng build

	앵귤러 프로젝트를 빌드하려면 ng build를 사용해야 하는데요 빌드가 되고나면 아까 지정한 아웃풋패스에 결과물이 들어잇을거에요
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`
	```
	ng build
	```

- logpresso.buildApp

	로그프레소쩜빌드앱 커맨드를 사용하면 ng build된 소스들을 번들 jar파일로 만들어 줍니다.

	그리고 빌드 후 생성된 번들 jar 파일을 install 해주고 refresh 후에 start 해 줍니다.

	최초 한번만 빌드앱을 해주면 로그프레소에 앱이 등록이 되기 때문에 소스를 수정할때마다 매번 번들을 인스톨 해줄 필요는 없구요 그때는 번들을 replace만 해주시면 되겠습니다. 

	```
	logpresso.buildApp /Users/mac/Documents/bootcamp-2019/bootcamp-app /Users/mac/Documents/bootcamp-2019/bootcamp-app/bootcamp-app-1.0.0.jar
	bundle.install file:///Users/mac/Documents/bootcamp-2019/bootcamp-app/bootcamp-app-1.0.0.jar
	bundle.refresh
	bundle.start 113
	```

- 맵핑된 URL(step2에서 등록한 app_id)

	httpd.contexts 커맨드는 서블릿에 맵핑된 URL을 확인할때 사용하시면 되구요
	```
	httpd.contexts
	```
	<img src="./image/servlet.png">

- 등록된 프로그램(step2에서 등록한 program_id)

	dom.programs localhost는 program_id 를 확인할때 사용하시면 되겠습니다.
	```
	dom.programs localhost
	```
	<img src="./image/programs.png">

- 메뉴에 추가된 앱 확인.

	이제 브라우저를 열고 주소창에 `localhost:8888` 입력해 볼게요. 

	여기까지 잘 진행하셨다면 대메뉴 가장 오른쪽에 방금 만든 앱이 추가가 되있을 겁니다. 브라우저를 열고 직접 확인해 보세요.

	8888뒤에 주소가 bootcamp/bootcamp로 되어있을 건데요 이부분은 step2에서 지정한 app_id/progrma_id 입니다.

		
	```
	localhost:8888
	```

	이제 절반정도 진행된것 같은데요 잠시 쉬는시간을 갖고 이어서 진행할게요

## 6. eediom-sdk, material-cdk

	- eediom-sdk
	이전 step까지 앱을 생성하고 빌드하고 로그프레소에 추가까지 해보신거구요. 여기부터는 스나리오 기반 데이터 연동에 필요한 부분입니다. 
	
	이디엄 sdk를 임포트해서 사용하는 부분은 좀 이따가 다시 나올거구요. 이디엄 sdk랑 머티리얼cdk 두개다 설치를 해 주세요.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`

	```
	$ yarn add https://github.com/logpresso/eediom-sdk.git#v1.0.5
	```
 
- material-cdk

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`

	```
	$ ng add @angular/material
	```
	```
	? Choose a prebuilt theme name, or "custom" for a custom theme: Indigo/Pink    // 테마 색상 선택
	? Set up HammerJS for gesture recognition?                                     // 제스처 라이브러리
	? Set up browser animations for Angular Material?                              // material 애니메이션 추가
	```


## 7. tsconfig.json, package.json

- tsconfig.json

	여기서는 타입스크립트가 컴파일 되어질 ecma스크립트 버전을 수정해 주겠습니다. target과 lib를 수정합니다.
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/tsconfig.json`


- package.json

	bin과 ,script 부분을 수정해 줍니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/package.json`


	```

## 8. app.module.ts, app.component.ts

- app.module.ts

	앵귤러 애플리케이션은 여러 모듈들의 집합이라고 할 수 있는데요 
	
	그 중에서도 이 app모듈은 최상위에 정의되어 있어야 하는 유일한 모듈, 루트모듈 입니다.
	
	이곳에서 하위 모듈들을 임포트 하고 컴포넌트에 객체를 주입해 주죠. 간단하게 살펴보겠습니다. 여러 모듈들을 관리하기 위해 @NgModule 장식자를 이용해 모듈을 구성하는데요 

	디클라레이션즈 속성에는 컴포넌트나 파이프를 선언해주고, 임포트에는 Angular가 브라우저 위에서 동작하기위해 필요한 브라우저모듈, 애플리케이션에서 라우팅을 수행하는 라우팅 모듈,
	
	템플릿에서 사용하는 NgModel지시자 등이 포함된 폼 모듈 을 선언해 줍니다.
		
	프로바이더에는 애플리케이션 전역에서 사용할 서비스를 등록해주고, 부트스트랩에는 최상위 앱컴포넌트를 등록한 다음 앱모듈을 export 해주면 되겠습니다.

	예제에서는 이디엄sdk의 서비스 모듈과, 쿼리 서비스를 사용하기 위해 앱모듈에 등록을 해주었습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.module.ts`

	```
	import { BrowserModule } from '@angular/platform-browser';
	import { NgModule } from '@angular/core';
	import { AppRoutingModule } from './app-routing.module';
	import { AppComponent } from './app.component';
	import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
	import { ServiceModule, QueryService } from 'eediom-sdk';
	import { FormsModule } from '@angular/forms';
	@NgModule({
		declarations: [
			AppComponent
		],
		imports: [
			BrowserModule,
			AppRoutingModule,
			BrowserAnimationsModule,
			FormsModule,
			ServiceModule.forRoot({
				productName: 'Araqne'
			})
		],
		providers: [QueryService],
		bootstrap: [AppComponent]
	})
	export class AppModule { }
	```


- app.component.ts

	앱 컴포넌트에서는 클래스 영역에 템플릿 데이터 출력과 관련된 로직을 작성해 주시면 되는데요.

	예제에서는 바인딩 변수들를 이용해 템플릿에서 화면제어를 하도록 했구요 
	템플릿으로부터 받은 클릭 이벤트에 대한 처리를 수행하는 함수 executeQuery를 등록했습니다.
	함수 내부에서는 이디엄sdk에 정의된 쿼리 서비스를 이용해 서버에 요청한 결과를 받아 템플릿에 필요한 데이터를 반영했습니다.
	
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.component.ts`

	```
	import { Component } from '@angular/core';
	import { QueryService } from 'eediom-sdk';
	@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.less']
	})
	export class AppComponent {
		title = 'bootcamp';
		query:string = '';
		result:any = [];
		runQuery:boolean = false;

		constructor(private queryService: QueryService) {
		}

		executeQuery() {
			this.queryService.query(this.query, 100, 0).then((res) => {
			this.result = res.records;
			this.runQuery = true;
			});
		}
	}
	```


## 9. app.component.html

- app.component.html

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.component.ts`


	템플릿을 간단하게 살펴보면 사용자로부터 입력받은 쿼리를 ngModel지시자를 통해 클래스 내부와 템플릿의 pre태그 내부에 양방향 바인딩 되었고 
	클릭이벤트로 클래스에 정의된 함수를 실행해 runQuery를 true로 변경하고 아래 ngIf가 동작하도록 처리했구요.
	내부에는 쿼리 결과를 ngFor지시자를 이용해 출력하고 이때 오브젝트 타입으로 찍히기 때문에 json 파이프를 사용해 출력했습니다.
- eediom-sdk 구버전 적용(eediom-sdk 수정전 임시사용)

	다운로드 폴더에 eediom-sdk 압축풀기.
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`

	```
	yes | cp -rf ../../../../../../Downloads/sdk ./node_modules/eediom-sdk
	```

	```
		<div class="toolbar" role="banner">
		<span>Bootcamp</span>
	</div>

	<div class="content" role="main">
		<div class="card highlight-card card-small">

	<input style="width:600px;" type="text" [(ngModel)]="query">
	<button (click)="executeQuery()">쿼리 실행</button>
	</div>
		<h2>Query</h2>
		<div class="terminal">
			<pre>{{query}}</pre>
		</div>
		<h2 *ngIf="runQuery"> Result</h2>
		<div *ngIf="runQuery" class="terminal">
				<pre *ngFor="let item of result">{{item | json}}</pre>
		</div>
	</div>
	```

- pom.xml 수정(eediom-sdk 수정전 임시사용)
	


## 10. 메이븐 빌드, 번들 교체

- mvn clean install, bundle.replace

	pom.xml 파일이 위차한 경로에서 빌드를 해주세요.

	빌드 후 생성된 번들을 replace 하고 refresh 해 줍니다.
	>`/bootcamp-2019/bootcamp-app/`

	```
	mvn clean install
	bundle.replace 113 file:///Users/mac/Documents/bootcamp-2019/bootcamp-app/target/bootcamp-app-1.0.0.jar
	bundle.refresh
	```


## 11. 시나리오 기반 데이터 연동

- parse event
	
	```
	localhost:8888
	```
	
	생성된 앱의 인풋 박스에 쿼리를 실행하고 결과를 출력한다.
	```
	bootcamp name=test  query="search index=github"  | parse event
	```
	<img src="./image/bootcamp.png">
