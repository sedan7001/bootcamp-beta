# bootcamp-2019

안녕하세요 빅데이터 플랫폼 팀의 김형태 입니다. 참석하신 모든분들이 짧은 시간안에 앱을 만들어 보시게 될 건데요. 이번 교육 시간이 조금이라도 도움이 될수 있으면 좋겠습니다. 이번시간은 총 10개의 스텝으로 준비를 해 봤습니다. 자그럼 첫번째 스텝부터 바로 시작해 보도록 하겠습니다. 스텝1클릭

## step1 배우는 것들

- 애플리케이션 생성, 빌드, 로그프레소에 추가
- 프레임워크는 앵귤러 8
	- 타입스크립트
	- 컴포넌트
	- 모듈
	- 템플릿
	- 라우터
	- 서비스
	- 컴포넌트 간 통신
- 튜토리얼 앱 만들기
- 시나리오 기반 데이터 앱에 연동
- step1 ~ step10 git branch
https://github.com/logpresso/bootcamp-2019
	
	여기서부터는 참가자 여러분들이 직접 타이핑을 하시면서 실습을 하시게 될 건데요 여러분들이 직접 앱을 생성하고 빌드해서 로그프레소에 추가하는 부분까지 진행해 보겠습니다.
	
	애플리케이션 개발에 사용할 앵귤러 프레임워크 버전은 가장 최신인 8.0 을 사용하겠습니다. 

	앱을 개발하실때 조금이라도 쉽고 빠르게 접근하실수 있도록 핵심개념들 위주로 준비해 봤습니다. 

	앵귤러는 타입스크립트로 작성된 프레임워크입니다. 앵귤러를 경험해보지 않으신 분들도 있을텐데요. 타입스크립트는 자바스크립트를 포함한 확장된 언어이기 때문에 기존의 es5 문법으로도 작업이 가능합니다. 
	
	뒷부분에는 튜토리얼 식으로 조그만 앱을 만들어 볼건데요 이번 부트캠프 시나리오 기반 데이터를 앱에 연동해서 보여주는 부분까지 진행을 하게 되겠습니다.

	이번 실습에 필요한 코드들은 모두 깃헙 리파지토리에 준비되어 있구요 step1번 브랜치부터 step10번 브랜치까지 올라가 있습니다. 중간에 하시다가 막힌다거나 하시면 해당 브랜치를  사용하시면 되겠습니다.
	
	그럼 이제 터미널을 열어주시고요 스텝1번부터 타이핑 해보겠습니다.


## step 2. createAppProject

- ### sh ./run.sh

- ### logpresso.createAppProject
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

- ### manifest.json
	- 앱이 실행될 때 시작 파라미터와 같은 기본값

	- step1에서 createAppProject를 하면 bootcamp-app 폴더에 자동생성.

	이 항목들은 필수로 입력되어야 하구요 이 항목들이 누락되면 페이지를 찾을 수 없다거나 404에러가 나오게 될겁니다. 각각의 속성들을 통해 패키지의 고유성을 판별하게 되고 로그프레소에서 인식하는 경로와 매칭을 시켜줍니다. program_name는 나중에 실제로 대메뉴에 추가되는 메뉴 이름이구요, program_id는 angular.json의 빌드경로와 일치시켜 줘야하고 app_id는 서블릿에서 url맵핑이 되는 속성입니다. 브라우저 url에서는 app_id/program_id로 순서로 보이게 됩니다. 
	
	앞에서 createAppProject를 하면 bootcamp-app 폴더에 자동으로 생성이 되었을 건데요. 해당 경로로 파일을 복사해 주세요.
	
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
## step 3. Angular-cli로 프로젝트 생성, 빌드와 루트 path 설정

### Angular-cli 로 프로젝트 생성

- Angular-cli
	
	- 프로젝트 생성, 템플릿 자동생성, 개발서버, 테스트 등등
	
		앵귤러 cli는 프로젝트 생성, 템플릿 자동생성, 개발 서버, 테스트 등등 다양한 기능을 지원하기 때문에 앵귤러 프로젝트를 진행하는데는 필수 개발도구라고 보셔야 되구요. 개발하시기 전에 꼭 먼저 설치를 해주시면 되겠습니다. 만약 gui로 앵귤러 프로젝트를 생성하고 싶다면 예를 들어 웹스톰 같은 ide에서 앵귤러버전을 선택해서 프로젝트를 생성한다거나 컴포넌트를 추가한다거나 한다면 자동으로 필요한 파일들이 모두 생성이 되겠죠. 그러한 것들을 커맨드라인 환경에서 명령어로 사용 가능하게 해주는게 앵귤러 cli입니다. 진행하면서 cli를 활용하는 부분이 나오면 다시 설명을 드리겠습니다. 

	```
	yarn global add @angular/cli
	```
	```
	? Would you like to add Angular routing? Yes
	? Which stylesheet format would you like to use? Less
	```	

- ng 명령어

	설치가 완료되었으면 이제 ng 명령어를 사용할 수 있는데요. ng버전, 그리고 ng뉴부트캠프 까지 실행을 해 주세요.

	```
	ng --version
	```

- ng new

	- 프로젝트 생성
		
		이부분은 라우터를 추가하겠냐고 물어보는 건데요. 페이지 이동을 위해는 라우터 모듈이 필요하기 때문에 여기서는 예스를 선택해 주세요.
		그다음은 css전처리기를 선택할 수 있는 부분입니다. 이러한 사스나, 레스를 활용하면 스타일시트를 작성할때에도 함수, 변수, 조건문 등의 프로그래밍이 가능해 집니다. 저희회사 사내에서는 less를 사용하고 있구요. 뒤에서 나오는 예제 앱에서 다시한번 설명드리겠습니다. 여기서는 less를 선택해 줍니다.
	
	>`/bootcamp-2019/bootcamp-app/src/main/`
	```
	ng new bootcamp
	```
	
### 빌드 경로와 루트 경로 수정
- angular.json

	- 폴더명은 step2에서 지정한 program_id
	
		angular.json이랑 index.html 파일을 수정해 주세요.
		outputPath 경로는 앵귤러 프로젝트가 빌드된 결과물이 저장되는 경로입니다. 이부분은
		여기 outputPath는 step2에서 지정한 program_id랑 일치시켜줘야 되는 부분이구요.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/angular.json`
	```
	"outputPath": "../resources/WEB-INF/bootcamp",
	```

- index.html

	- 로그프레소에서 인식하는 루트경로
	
		위에서 설정한 outputPath경로명은 bootcamp였구요 이것은 program_id 였죠. 이부분이 최종 url경로 이기 때문에 base경로는 하위경로가 아닌 현재 경로 bootcamp를 바라봐야 하는 부분입니다.
	
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/index.html`
	```
	<base href="./">
	```
---


## step 4. 빌드, 로그프레소 메뉴에 앱 추가하기
- ng build
	- 앵귤러 프로젝트 빌드
	- outputPath에 저장
	
		앵귤러 프로젝트를 빌드하려면 ng build를 사용해야 하는데요 빌드가 되고나면 아까 지정한 아웃풋패스에 결과물이 들어잇을거에요
	
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`
	```
	ng build
	```

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`
	```
	ng build
	```

- logpresso.buildApp
	- ng build된 소스들을 번들jar 파일로 
	- jar 파일을 install,refresh,start
	- 최초 한번만 빌드, 수정시 번들교체

		logpresso.buildApp 커맨드를 사용하면 ng build된 소스들을 번들 jar파일로 만들어 줍니다.
		그리고 빌드 후 생성된 번들 jar 파일을 install 해주고 refresh 후에 start 해 줍니다.
		최초 한번만 빌드앱과 번들인스톨을 해주면 로그프레소에 앱이 등록이 되기 때문에 소스를 수정할때마다 매번 번들을 인스톨 해줄 필요는 없구요 그때는 번들을 replace하고 refresh만 해주시면 되겠습니다. 

	```
	logpresso.buildApp /Users/mac/Documents/bootcamp-2019/bootcamp-app /Users/mac/Documents/bootcamp-2019/bootcamp-app/bootcamp-app-1.0.0.jar
	bundle.install file:///Users/mac/Documents/bootcamp-2019/bootcamp-app/bootcamp-app-1.0.0.jar
	bundle.refresh
	bundle.start 113
	```
	

	```
- ### 맵핑된 URL(app_id)
	- logpresso.buildApp, bundle.install 하면  telnet 에서 app_id, program_id 조회 가능

		logpresso.buildApp, bundle.install을 하고나면 telnet상에서 app_id, program_id를 확인하실 수 있습니다.	만약 잘못 설정이 되어있다면 bundle.uninstall 000 다시 설정해준 후에 빌드앱과 번들인스톨을 해주시면 되겠습니다.
	```
	httpd.contexts
	```

- ### 등록된 프로그램(program_id)

	```
	dom.programs localhost
	```

- ### 로그프레소 메뉴에 앱이 추가되었는지 확인.

	- 브라우저 주소창에 입력
	
		이제 브라우저를 열고 주소창에 `localhost:8888` 입력해 볼게요. 
		여기까지 잘 진행하셨다면 대메뉴 가장 오른쪽에 방금 만든 앱이 추가가 되있을 겁니다. 브라우저를 열고 직접 확인해 보세요.
	```
	localhost:8888
	
	```
# step 5. eediom-sdk

### eediom-sdk

- 시나리오 기반 데이터 연동에 필요한 부분

	이전 step까지 앱을 생성하고 빌드하고 로그프레소에 추가까지 해보신거구요. 여기부터는 스나리오 기반 데이터 연동에 필요한 부분입니다. 
	이디엄 sdk를 임포트해서 사용하는 부분은 뒷부분에서 설명드리도록 하겠습니다. 이디엄 sdk를 설치해주세요.
	
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/`

	```
	$ yarn add https://github.com/logpresso/eediom-sdk.git#v1.0.6
	```

### tsconfig.json

- 타입스트립트가 컴파일 될 ecmaScript 버전 수정.
- target, lib 수정

	lib속성에는 개발시 지원되는 ecma스크립트 버전을 작성해 주고 target 속성에는 컴파일 된 후의 버전을 적어줍니다. 개발시에는 최신버전의 자바스크립트에서 지원하는 여러 기능을 활용하고 컴파일 후에는 브라우저 호환성을 보장하기 위해 taget에 es5를 적어주시면 되겠습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/tsconfig.json`


	```
		"target": "es5",
		],
		"lib": [
		"es2015",
		"es2016",
		"es2017",
		"es2018",
		"dom"
		]
	```

### package.json

- bin, script 수정.

bin 속성에는 우리가 개발에 사용할 node와 yarn과 angular-cli의 경로를 설정해 주었습니다. script 의 start속성에는 개발서버를 열었을때 자동으로 브라우저 탭이 열리는 옵션과 기본경로를 설정해 주었구요, build속성 에는 빌드시 해시태그를 사용하지 않고 product모드로 빌드를 하고, 빌드 최적화툴을 사용하지 않겠다는 옵션을 주었습니다. 이제 bin 과 script영역을 수정해 보겠습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/package.json`
	

	```
	"bin": {
		"node": "node/node",
		"yarn": "node/yarn/dist/bin/yarn",
		"ng": "node/node node_modules/@angular/cli/bin/ng"
	},
	"scripts": {
		"ng": "ng",
		"start": "ng serve --base-href=/ --open",
		"build": "ng build --output-hashing=none --prod --build-optimizer=false",
		"test": "ng test",
		"lint": "ng lint",
		"e2e": "ng e2e"
	```



## step 6. app.module.ts, app.component.ts

### 6-1. app.module.ts

- 앵귤러 애플리케이션은 여러 모듈들의 집합.
- 루트 모듈.
- 하위 모듈 임포트.


	앵귤러 애플리케이션은 여러 모듈들의 집합이라고 할 수 있는데요 
	그 중에서도 이 app모듈은 최상위에 정의되어 있어야 하는 유일한 모듈, 루트모듈 입니다.
	
	이곳에서 하위 모듈들을 임포트 하고 컴포넌트에 객체를 주입해 주죠. 코드를 살펴보겠습니다. 여러 모듈들을 관리하기 위해 @NgModule 장식자를 이용해 모듈을 구성하는데요 

	디클라레이션즈 속성에는 컴포넌트나 파이프를 선언해주고, 임포트에는 Angular가 브라우저 위에서 동작하기위해 필요한 브라우저모듈, 애플리케이션에서 라우팅을 수행하는 라우팅 모듈,
	
	템플릿에서 사용하는 ngModel지시자 등이 포함된 폼 모듈, ngIf, ngFor지시자 등이 포함된 common 모듈을 선언해 줍니다.
		
	프로바이더에는 애플리케이션 전역에서 사용할 서비스를 등록해주고, 부트스트랩에는 최상위 앱컴포넌트를 등록한 다음 앱모듈을 export 해주면 되겠습니다.

	예제에서는 이디엄sdk의 서비스 모듈과, 쿼리 서비스를 사용하기 위해 앱모듈에 등록을 해주었습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.module.ts`

	```
	import { BrowserModule } from '@angular/platform-browser';
	import { NgModule } from '@angular/core';
	import { AppRoutingModule } from './app-routing.module';
	import { AppComponent } from './app.component';
	import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
	import { ServiceModule, QueryService, GridModule, ChartModule } from 'eediom-sdk';
	import { FormsModule } from '@angular/forms';
	import { CommonModule } from '@angular/common';

	@NgModule({
		declarations: [
			AppComponent,
		],
		imports: [
			BrowserModule,
			AppRoutingModule,
			BrowserAnimationsModule,
			ServiceModule.forRoot({
				productName: 'Araqne'
			}),
			FormsModule,
			GridModule,
			ChartModule,
			CommonModule
		],
		providers: [QueryService],
		bootstrap: [AppComponent]
	})
	export class AppModule { }
	```


### 6-2. app.component.ts

- 클래스 영역에 템플릿 데이터 출력 로직

	앱 컴포넌트에서는 타입스크립트로 작성된 것을 확인할 수 있는데요 다음과 같이 클래스를 생성할 수 있고 모든 변수들에는 타입을 지정해 줍니다. 
	
	값을 반환하지 않는 함수에는 void를, 반환하는 함수는 리턴하는 타입을 지정해 주면 됩니다. 컴포넌트는 크게 임포트 영역, 컴포넌트 장식자 영역, 클래스 영역으로 이루어져 있는데요. 
	
	임포트 영역에서는 컴포넌트 내부에서 필요한 항목들을 임포트 해주고, 컴포넌트 영역에서는 해당 컴포넌트를 호출할때 사용하는 셀렉터, index.html에서 처럼 이렇게 사용이 되구요. 템플릿 경로와 스타일시트의 경로를 지정해 줄 수 있습니다.
	
	클래스 영역에서는 템플릿 데이터 출력과 관련된 로직을 작성해 주시면 되는데요.예제에서는 바인딩 변수들를 이용해 템플릿에서 화면제어를 하도록 했구요 
	템플릿으로부터 받은 클릭 이벤트에 대한 처리를 수행하는 함수 executeQuery를 등록했습니다.
	
	함수 내부에서는 이디엄sdk에 정의된 쿼리 서비스를 이용해 서버에 요청한 결과를 받아 템플릿에 필요한 데이터를 반영해주고 그 값은 다시 onRender함수에서 차트의 시리즈에 필요한 데이터로 필터링해 배열로 만들었습니다.
	
	이 과정이 너무 짧은 시간에 끝나기 때문에 예제에서는 1000밀리 세컨드 동안 로딩이미지를 보여주고 마지막에 차트와 그리드를 화면에 그려주었습니다.
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.component.ts`

	```
	import { Component, NgZone, ViewChild } from '@angular/core';
	import { QueryService, SubscribeTypes } from 'eediom-sdk';
	import { GridData, QueryResult, ChartComponent, ChartTypes, LineChartConfigs, Field, Chart } from 'eediom-sdk';
	@Component({
		selector: 'app-root',
		templateUrl: './app.component.html',
		styleUrls: ['./app.component.less'],
	})
	export class AppComponent {
		title: string = 'BOOTCAMP 2019';
		link: string = 'LOGPRESSO';
		@ViewChild('chart', { static: true }) chartComponent: ChartComponent;
		gridData: GridData;
		fieldTypes: QueryResult["fieldTypes"];
		records: QueryResult["records"];
		count: QueryResult["count"];
		chart: Chart;
		query: string = '';
		loading: boolean = false;
		querySuccess: boolean = false;
		isOpen: boolean = false;


		constructor(private queryService: QueryService, private ngZone: NgZone) {
		}

		ngOnInit() {
			this.chart = new Chart(ChartTypes.Area, new LineChartConfigs(
				new Field('_time', 'date', '날짜'),
				[
					new Field('Unreal.js', 'int'),
					new Field('billboard.js', 'int'),
					new Field('iotjs', 'int'),
					new Field('metatron-discovery', 'int'),
					new Field('tui.editor', 'int'),
					new Field('veles', 'int'),
				],
				false
			));
			this.chartComponent.render(null, this.chart);
		}

		executeQuery() {
			this.querySuccess = false;
			this.loading = true;
			this.queryService.query(this.query, (queryId, subscribeData) => {
				if (subscribeData.type === SubscribeTypes.Eof) {
					this.queryService.getResult(queryId, 100, 0).then((queryResult) => {
						this.ngZone.run(() => {
							this.fieldTypes = queryResult.fieldTypes;
							this.count = queryResult.count;
							this.records = queryResult.records;
							this.onRender();
						})
					})
				}
			});
		}

		columnFiltering(columns) {
			const tmp = columns.filter((key) => {
				return key.column !== '_id' && key.column !== '_time' && key.column !== '_table';
			}).map((key) => {
				return new Field(key.column, key.type);
			});
			return tmp;
		}

		onRender(): void {
			setTimeout(() => {
				const filteredColumns = this.columnFiltering(this.fieldTypes);
				this.chart = new Chart(
					ChartTypes.Area, 
					new LineChartConfigs(new Field('_time', 'date', '날짜'), filteredColumns, false)
					);

				this.loading = false;
				this.querySuccess = true;
				this.isOpen = true;
				this.chartComponent.update(this.chart, this.records);
				this.gridData = new GridData({
					records: this.records
				})
			}, 1000)
		}

	}
	```
## step 7. app.component.html

- app.component.html

	템플릿을 코드를 살펴보겠습니다. 템플릿은 헤더영역과 사용자 입력을 받는 폼 영역, 로딩 이미지영역, 차트와 그리드 영역으로 구성했습니다. 앞서 살펴본 앵귤러 아키텍쳐에서 컴포넌트와 템플릿은 바인딩을 통해 값을 주고 받는다고 했습니다. 대괄호를 이용해 컴포넌트로부터 프로퍼티를 바인딩 받을 수 있고, 소괄호를 통해 템플릿으로부터 컴포넌트로 이벤트를 바인딩 할 수 도 있습니다. 이렇게 두 괄호를 이용해 양방향 바인딩도 가능한데요. link부분을 query로 변경하고 쿼리를 입력해 보겠습니다. 사용자로부터 입력받은 쿼리를 ngModel지시자를 통해 컴포넌트의 클래스 내부에 바인딩 되었고 다시 span태그 내부에 바인딩 되어 헤더 영역에 출력된 것을 확인할 수 있습니다. 여기서 loading과 querySuccess를 이용해 화면을 제어했는데요 두가지 방법은 어떤 차이가 있을까요?
	먼저 visivility는 css속성으로 가시성을 결정하는데요 이는 화면에서 보이지 않을 뿐이지 dom에는 존재하고 있습니다. 이에반에 ngIf 지시자는 조건에 해당하지 않을 때에는 dom에서 완전히 제거가 되어 메모리에서 사라지게 됩니다. 
	예제에서는 쿼리를 입력받은 후 클릭이벤트로 클래스에 정의된 함수를 실행해 loading을 true로 변경해서 로딩중 이미지가 보여지게 했구요. 쿼리가 성공적으로 종료되어 querySuccess가 트루가 되면 차트와 그리드가 보여지게 했습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.component.ts`

	<details>
	<summary>app.component.html</summary>
	<div markdown="1">

	```
	<div class="page-wrapper">
		<div class="wrapper">
			<div class="inner">
				<header id="header">
					<a class="main">
						<span>{{title}}</span>
					</a>
					<a class="link">
						<span>{{link}}</span>
					</a>
				</header>
			</div>
		</div>

		<div class="wrapper">
			<div class="inner">
				<section class="main accent"
					[style.animation]="isOpen ? 'section-animation 1.2s 0.3s 1 ease-in-out forwards': 'none'">
					<header class="major">
						<h2>query</h2>
					</header>
					<form (ngSubmit)="executeQuery()" class="combined"
						[style.animation]="isOpen ? 'form-animation 1s 1.1s 1 ease-in-out forwards': 'none'" autocomplete="off">
						<input [(ngModel)]="query" name="query" type="text" placeholder="query here">
						<input type="submit" value="run">
					</form>
				</section>
			</div>
		</div>
		<div class="wrapper loading" *ngIf="loading">
			<div class="inner">
				<section class="main">
					<header class="major">
						<img src="assets/loading.gif" />
					</header>
				</section>
			</div>
		</div>
		<div class="wrapper sdk" [style.visibility]="querySuccess ? 'inherit': 'hidden'"
			[style.animation]="isOpen ? 'sdk-animation 1s 1.5s 1 ease-in-out forwards': 'none'">
			<div class="inner">
				<section class="main">
					<header class="major">
						<h2>area chart</h2>
					</header>
					<div class="spotlights">
						<article>
							<edm-chart #chart></edm-chart>
						</article>
					</div>
				</section>
				<section class="main">
					<header class="major">
						<h2>grid</h2>
					</header>
					<div class="spotlights">
						<article>
							<edm-grid [gridData]="gridData" [pageSize]="100" [currentPage]="1" [showPager]="false">
							</edm-grid>
						</article>
					</div>
				</section>
			</div>
		</div>

	</div>

	```
	</div>
	</details>

- index.html

이제 이 앱에 스타일을 입혀보도록 하겠습니다. 
먼저 구글 폰트를 링크태그를 통해 불러와 줍니다.

>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/index.html`
	
	add google font


		<link href='http://fonts.googleapis.com/css?family=Raleway:300,500,600' rel='stylesheet' type='text/css'>


- styles.less

다음은 글로벌 스타일 파일을 수정해 줍니다.

>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/styles.less`


	```
	html, body { height: 100%; }
	body { margin: 0; }

	```

- loading.gif

로딩중 이미지를 해당경로에 넣어줍니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/assets/loading.gif`
	
	
	https://github.com/sedan7001/angular8-bootcamp/blob/master/src/main/bootcamp2/src/assets/loading.gif?raw=true
	
	download loading image

- app.component.less

css전처리기인 less로 스타일을 만들었는데요. 간단히 살펴보면 다음과 같이 매개변수와 같이 변수를 사용할수 있고 이것은 mixin이라고 하는데요 함수처럼 사용할 수 있구요. 조건문도 사용할 수 있습니다. 아래 경로에 less 파일을 작성해 주세요.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/app.component.less`


## step 8. 프로젝트 전체 빌드 후 시나리오 기반 데이터 

### 8-1. 메이븐 빌드하기
- pom.xml
	
	pom.xml 파일이 위차한 경로에서 빌드를 해주세요.
	앞서 했던 빌드와 다른점은 ng build하고 logpresso.buildApp을 하면 앵귤러 프로젝트만 빌드가 된것이고 지금은 메이븐 빌드로 서버 코드까지 한번에 빌드를 한 것입니다. 아까 말씀드린 것처럼 앞에서 이미 번들을 인스톨 하였기 때문에 이제는 빌드 후 생성된 번들을 replace 하고 refresh 만 해 줍니다.
	>`/bootcamp-2019/bootcamp-app/`

	```
	mvn clean install
	bundle.replace 113 file:///Users/mac/Documents/bootcamp-2019/bootcamp-app/target/bootcamp-app-1.0.0.jar
	bundle.refresh
	```
### 8-2. 시나리오 기반 데이터를 앱에 출력

- parse event
	
	
	```
	localhost:8888
	```
	
	
	```
	table trends | rename date as _time
	```	

	
	
## 9. 라우터로 라우팅 구현하기, 컴포넌트를 하위 컴포넌트로 분리하기
- ### ng serve
	이제 angular-cli가 지원하는 개발서버를 열고 진행을 해보겠습니다. 여기서부터는 4200번 포트에서 변경사항을 실시간으로 확인할 수 있습니다.

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp`

- ### ng g c main
	아래 경로에서 ng g c main을 입력해 줍니다. 자동으로 생성된 파일들이 보이시죠? angular-cli가 자동으로 컴포넌트에 필요한 파일들을 생성해 준 것입니다. 
	- 아래 경로에서 `ng g c main`
		>`/bootcamp-2019/bootcamp-app/src/main/bootcamp`
	
- ### ng g c trend
	- 아래 경로에서 `ng g c trend`
		>`/bootcamp-2019/bootcamp-app/src/main/bootcamp`
	
- ### app.module.ts 확인
	- angular cli 로 생성 -> 앱 모듈에 자동 등록.
	
	앱 모듈을 확인해 보면 이렇게 방금 만든 main컴포넌트와 trend컴포넌트가 자동으로 등록된 것을 확인할 수 있습니다. 	
	
- ### main.component.ts

	1. `app.component.ts` 내용을 전체 복사해서 `main.component.ts` 에 붙이기.
	2. `main.component.ts` 4~9번 라인만 아래와 같이 `main`으로 변경
	
	이제 컴포넌트를 분리해 볼 건데요 작성할 코드가 많으니 가이드를 보시면서 작성을 해 주시구요.이번스텝의 끝부분까지 코드를 작성한 후에 다시 살펴보도록 하겠습니다. 
	
		```
		@Component({
			selector: 'app-main',
			templateUrl: './main.component.html',
			styleUrls: ['./main.component.less'],
		})
		export class MainComponent {
		```

- ### app.component.ts
	1. `app.component.ts` 의 `title`, `link` 변수(10번~11번 라인)을 제외한 모든 코드 삭제.
	2. `app.component.ts` 임포트 영역에서 `Component` 를 제외하고 모드 삭제.


- ### main.component.less
	- `app.component.less` 파일의 100번 ~ 361번 라인을 잘라내서 `main.component.less`에 붙이기

	
- ### main.component.html
	1. `main.component.html` 파일의 내용을 비운다.
	2. `app.component.html` 파일의 15번 ~ 64번 라인을 잘라내서 `main.componet.html`에 붙이기

- ### app.component.html
	- 5번, 8번 라인에 `routerLink`, `routerLinkActive` 추가하고 14번라인에 `router-outlet` 추가
	
		```
		<div class="page-wrapper">
			<div class="wrapper">
				<div class="inner">
					<header id="header">
						<a routerLink="/main" routerLinkActive="active" class="main">
							<span>{{title}}</span>
						</a>
						<a routerLink="/trend" routerLinkActive="active" class="link">
							<span>{{link}}</span>
						</a>
					</header>
				</div>
			</div>
			<router-outlet></router-outlet>
		</div>
		```
	

- ### app-routing.mopdule.ts
	먼저 앱컴포넌트는 헤더 영역만 남겨두고 모두 메인 컴포넌트로 옮겼습니다. 그리고 트렌트 컴포넌트를 추가해 주었구요. 두 컴포넌트를 앱 라우팅 모듈에 등록해 주었습니다. 라우터에서는 브라우저의 url에 찍힐 경로와 해당하는 컴포넌트들을 등록해주었는데요 3번째 라인은 기본경로로 들어왔을때 라우팅되는 컴포넌트를 지정해 주었고 4번째 라인은 위에서 지정한 경로와 일치하지 않는 경로로 들어왔을때 보이는 404페이지같은 컴포넌트를 등록해 주면 되는데 여기서는 따로 만들지는 않고 메인컴포넌트로 연결해 주었습니다. 
	그리고 앱 컴포넌트의 템플릿을 보면 a링크에 라우터 링크를 걸어 클릭시에 라우터에 등록된 해당 컴포넌트가 router-outlet영역에 보여지게 했습니다.
	- 5번, 8번 라인에 `routerLink`, `routerLinkActive` 추가하고 14번라인에 `router-outlet` 추가
	
		```
		import { NgModule } from '@angular/core';
		import { Routes, RouterModule } from '@angular/router';
		import { MainComponent } from './main/main.component';
		import { TrendComponent } from './trend/trend.component';

		const routes: Routes = [
			{ path: 'main', component: MainComponent },
			{ path: 'trend', component: TrendComponent },
			{ path: '', redirectTo: 'main', pathMatch: 'full' },
			{ path: '**', component: MainComponent }
		];

		@NgModule({
			imports: [RouterModule.forRoot(routes)],
			exports: [RouterModule]
		})
		export class AppRoutingModule { }
		```

	
## 10. 서비스 구현, 컴포넌트간 값 전달.

이번 시간에는 미리 만들어져있는 이디엄sdk의 서비스가 아닌 재사용 가능한 단일 데이터 서비스를 만들어 보도록 하겠습니다. 이렇게 별도의 서비스를 사용하면 컴포넌트를 간결하게 유지하고 뷰에 주력할수 있는 장점이 있습니다. 그리고 mock서비스로 컴포넌트 테스트를 해보도록 하겠습니다. 먼저 1번과 2번을 작성해 주세요. 서비스라는 폴더를 하나 생성하셔야 합니다. TRENDS배열은 Trend클래스를 사용하기 때문에 임포트하고 타입을 지정해 주었습니다. TRENDS상수는 다른곳에서 임포트 할 수 있도록 익스포트 해 줍니다.
### 10-1. trend 클래스 생성.
- 아래 경로에 `trend.ts` 파일 생성.
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/service`

	```
	export class Trend {
			'Unreal.js' : number;
			'billboard.js': number;
			'_table': string;
			'metatron-discovery': number;
			'veles': number;
			'tui.editor': number;
			'_id': number;
			'iotjs': number;
			'_time': string
	}
		```
### 10-2. trend 클래스 생성.
- 아래 경로에 `trend.mock.ts` 파일 생성.
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/service`

	```
	import { Trend } from './trend';

	export const TRENDS: Trend[] = [
		{
			"Unreal.js": 1,
			"billboard.js": 3,
			"_table": "trends",
			"metatron-discovery": null,
			"veles": null,
			"tui.editor": 3,
			"_id": 61,
			"iotjs": 1,
			"_time": "2018-06-25 00:00:00+0900"
		},
	]
	```
### 10-3. trend 서비스 생성.

- 아래 경로에서 `ng g s trend`

	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/service`

- trend.service.ts

	서비스를 생성해 보겠습니다. ng g s trend 명령어를 입력해주세요. mock TRENDS를 가져와서 getTrends()메서드를 통해 리턴하게 했습니다. 지금은 목 서비스 이지만 실제로 사용자가 서버가 응답할때까지 기다릴 필요는 없겠죠. 응답에 따라 뷰를 조정하기 위해 프로미스를 사용했습니다. 이제 비동기 서비스 작업이 이행되면 resolve가 실행되어 프로미스를 반환하게 됩니다. 
	```
	import { Injectable } from '@angular/core';
	import { Trend } from './trend';
	import { TRENDS } from './trend.mock';

	@Injectable({
		providedIn: 'root'
	})
	export class TrendService {

		constructor() { }
		
		getTrends(): Promise<Trend[]> {
			return Promise.resolve(TRENDS);
		}
	}	
	```
### 10-4. app 모듈에서 서비스 임포트.
- app.module.ts 에서 서비스를 import 하고 providers 속성에 등록.
	```
	import { BrowserModule } from '@angular/platform-browser';
	import { NgModule } from '@angular/core';

		],
		providers: [QueryService,TrendService],
		bootstrap: [AppComponent]
	})
	export class AppModule { }	
	```
		
### 10-5. trend-child 컴포넌트 생성.

	다음은 trend컴포넌트의 자식 컴포넌트인 trend-child컴포넌트를 만들어 보겠습니다. 5번부터 6번 코드를 작성해 주세요. 
	
	trend컴포넌트를 보겠습니다. ngOnInit은 앵귤러의 라이프사이클 함수중에 하나입니다. 이 컴포넌트가 이닛될때 실행되는 함수인데요 이곳에서 getTrends를 호출하여 프로미스가 이행되었고 then함수를 통해 리턴값을 parentTrends 배열에 설정했습니다. 이제 이 데이터를 자식 컴포넌트로 전달하려면 어떻게 해야할까요? 템플릿을 보면 app-trend-child 셀렉터를 통해 trend-child컴포넌트를 불러오고 있구요. 프로퍼티 바인딩으로 parentTrends값을 할당하고 있습니다. 
	
	이제 trend-child 컴포넌트를 보면 @Input장식자를 볼 수 있는데요 이를 통해 부모 컴포넌트로 부터 바인딩 된 값을 childTrends에 설정하게 되는 것입니다. 이제 템플릿을 보시겠습니다. pre태그에서는 childTrends 라는 배열을 받아 ngFor 지시자를 통해 반복적으로 그려주고 있습니다. 이 값은 object로 찍히기 때문에 앵귤러의 내장된 json파이프를 통해 출력하도록 했습니다.
	
- 아래 경로에서 `ng g c trend-child`
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/trend`
- trend-child.componet.ts
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/trend/trend-child/trend-child.component.ts`
	```
	import { Component, OnInit, Input } from '@angular/core';
	import { Trend } from '../../service/trend';

	@Component({
	  selector: 'app-trend-child',
	  templateUrl: './trend-child.component.html',
	  styleUrls: ['./trend-child.component.less']
	})
	export class TrendChildComponent implements OnInit {

		@Input() childTrends: Trend[];
		
	  constructor() { }
	  ngOnInit() {
	  }
	}
	```
- trend-child.component.html
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/trend/trend-child/trend-child.component.html`
	```
	<div class="wrapper">
		<div class="inner">
			<section class="main">
				<header class="major">
					<h2>trend-child component</h2>
				</header>
				<div class="spotlights" >
					<pre *ngFor="let trend of childTrends">{{trend|json}}</pre>
				</div>
				<div class='terminal'>
				</div>
			</section>
		</div>
	</div>	
	```
- trend-child.component.less
	>`/bootcamp-2019/bootcamp-app/src/main/bootcamp/src/app/trend/trend-child/trend-child.component.less`
	```
	.keyframes(@name) when (@name = trend-animation) {
	    @keyframes @name {
			from {
				opacity: 0;
				height: 0;
			}
			to {
				opacity: 1;
				height: 51em;
			}
		}
	}
	.keyframes(trend-animation);
	:host {
		font-size: 15px;
	}
	.wrapper {
		width: 100%;
		>.inner {
			width: 80em;
			max-width: 100%;
			margin-left: auto;
			margin-right: auto;
			>.main {
				background-color: #e8eafa;
				width: 50em;
				max-width: 100%;
				margin-left: auto;
				margin-right: auto;
				h2 {
					color: #434b56;
					line-height: 1.5;
					letter-spacing: 0.1em;
					font-size: 1.75em;
					font-weight: 800;
					margin: 0 0 0.65em 0;
				}
				header.major {
					text-align: center;
					margin-top: 3em;
					margin-bottom: 3em;
					transition: opacity .5s linear 2s;
				}
				.spotlights {
					box-shadow: 0 0 2em 0 rgba(0, 0, 0, 0.4);
					position: relative;
					border-radius: 6px;
					margin-top: 8px;
					height: 51em;
					overflow: auto;
					opacity: 0;
					-ms-overflow-style: none;
					&::-webkit-scrollbar {
						display: none;
					}
					animation: trend-animation 2.7s 0.3s 1 ease-in-out forwards;
				}
				pre {
					margin-top : 3em;
					width: 20em;
					max-width: 100%;
					margin-left: auto;
					margin-right: auto;
					color: #4e5266;
					transform:scale(1);
					transition:.4s;
					font-weight: 500;
				}
				pre:hover{
					transform:scale(1.1);
				}
			}
		}
	}
	```
### 10-6. trend 컴포넌트와 trend-child 컴포넌트간 통신.
- trend.componet.ts
	```
	import { Component, OnInit } from '@angular/core';
	import { TrendService } from '../service/trend.service';
	import { Trend } from '../service/trend';

	@Component({
		selector: 'app-trend',
		templateUrl: './trend.component.html',
		styleUrls: ['./trend.component.less']
	})
	export class TrendComponent implements OnInit {

		parentTrends:Trend[];
		
		constructor(private trendService: TrendService) { }

		ngOnInit(): void {
			this.getTrends();
		}

		getTrends(): void {
			this.trendService.getTrends().then(data => this.parentTrends = data);
		}
	}
	```

- trend.componet.html
	```
	<app-trend-child [childTrends]="parentTrends"></app-trend-child>
	```
### 10-7. 빌드 후 bootcamp 앱 확인
이제 마지막으로 메이븐 빌드후 앱을 확인해 보도록 하겠습니다.
텔넷에서 mvn clean install , bundle.replace, refresh를 해주세요.
화면에 잘 나오시나요? 이렇게 해서 여러분들이 튜토리얼 앱을 만들어보고 로그프레소에 앱을 추가해 보았습니다. 네 모두 수고 많으셨구요. 저는 여기까지 진행하도록 하겠습니다. 감사합니다. 

- 메이븐 인스톨, 번들 교체.

	>`/bootcamp-2019/bootcamp-app/`

	```
	mvn clean install
	bundle.replace 113 file:///Users/mac/Documents/bootcamp-2019/bootcamp-app/target/bootcamp-app-1.0.0.jar
	bundle.refresh
	```
		