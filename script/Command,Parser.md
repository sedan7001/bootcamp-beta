
# BootcampCommand / BootcampParser

**Table of Contents**
* [BootcampCommand](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#bootcampcommand)
	* [BootcampCommandParser.java](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#bootcampcommandparserjava)
	* [BootcampCommand.java](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#bootcampcommandjava)
* [BootcampParser](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#bootcampparser)
	* [SplunkGithubEventParserFactory.java](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#splunkgithubeventparserfactoryjava)
	* [SplunkGithubEventParser.java](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#splunkgithubeventparserjava)
	* [UI 에 적용](https://github.com/logpresso/bootcamp-2019/blob/master/script/Command%2CParser.md#ui-%EC%97%90-%EC%A0%81%EC%9A%A9)
## BootcampCommand

- `BootcampCommand.java`
- `BootcampCommandParser.java`

Splunk 로부터 데이터를 불러오기 위해 쿼리에서 `bootcamp` 라는 command 를 사용할 예정입니다.

`BootcampCommand` 에서는 `bootcamp` command 의 역할을 정의할 것이고,
`BootcampCommandParser` 에서는 `bootcamp` command 를 쿼리 창에 입력했을 때 쿼리문을 어떻게 해석할지 정의합니다.

---

### `bootcamp` 커맨드 스펙
커맨드 `bootcamp` 는 `name` 과 `query` 라는 인자 값을 받습니다.

`name` 에는 `SplunkProfile` (ssh 로 설정) 의 이름이 들어가야 하고 `query` 에는 Splunk 에 요청할 쿼리가 들어가면 됩니다.

``bootcamp name=splunk query="search index=github"`` 와 같은 형식입니다.

---

### `BootcampCommandParser.java`
쿼리문을 어떻게 해석할지 정의합니다.

`AbstractQueryCommandParser` 를 상속하고 `QueryParserService`, `ConfigService` 를 `@Require` 합니다.

```
@Component(name = "logpresso-bootcamp-command-parser")
public class BootcampCommandParser extends AbstractQueryCommandParser {
	@Requires
	private QueryParserService parserService;

	@Requires
	private ConfigService conf;
}
```

`start()`, `stop()` method 를 작성한 다음

```
@Validate
public void start() { // 커맨드 파서 시작
	parserService.addCommandParser(this);
}

@Invalidate
public void stop() { // 커맨드 파서 중지
	if (parserService != null)
		parserService.removeCommandParser(this);
}
```
`getCommandName()` 도 작성합니다.

```
@Override
public String getCommandName() { // 커맨드의 이름 가져오기
	return "bootcamp";
}
```

이제 커맨드를 파싱해주는 `parse(QueryContext context, String comandString)` method 를 작성합니다.

```
@Override
public QueryCommand parse(QueryContext context, String commandString) {	
	// parse command
}
```

`commandString` 에 쿼리 창에서 입력한 쿼리문이 들어가 있습니다

```
QueryTokenizer.parseOptions(QueryContext context, String commandString, int offset,
		List<String> validKeys, FunctionRegistry functionRegistry)
```
를 이용하여 `ParseResult` 를 얻습니다.

여기서 parameter 들은 다음과 같습니다.
- `offset` : 커맨드 이름의 길이
- `validKeys` : 파싱할 인자들의 리스트 (이 커맨드의 경우 `name`, `query`)
- `functionRegistry` : (Help Wanted)

```
ParseResult r = QueryTokenizer.parseOptions(context, commandString, getCommandName().length(),
		Arrays.asList("name", "query"), getFunctionRegistry());
```

결과로 얻은 `ParseResult` 안에는 커맨드의 인자들이 map 의 형태로 `value` 라는 필드에 담겨있고, 다음 커맨드의 시작 offset 이 들어 있습니다. `ParseResult` 의 map 을 가져와서 `name` 과 `query` 를 가져옵니다. `query` 의 경우 `trim()` 을 이용해 불필요한 공백은 없애줍니다.

```
Map<String, Object> options = (Map<String, Object>) r.value;
String name = (String) options.get("name");
String query = options.get("query").toString().trim();
```

이제 가져온 `name` 에 해당하는 `SplunkProfile` 을 가져와야 합니다.
confdb 에 `logpresso-bootcamp`  데이터베이스가 없으면 생성하여 가져옵니다.
가져온 `db` 에서 `findOne` 을 이용해 해당하는 데이터를 찾습니다.

```
ConfigDatabase db = conf.ensureDatabase("logpresso-bootcamp");
Config c = db.findOne(SplunkProfile.class, Predicates.field("name", name));
```

결과가 없는 경우를 대비하여 `QueryParseException` 처리를 해줍니다.
```
if (c == null) {
	Map<String, String> params = new HashMap<String, String>();
	params.put("name", name);
	throw new QueryParseException("909090", -1, -1, params);
}
```
이제 `SplunkProfile` 을 가져오고 ,
`BootcampCommand` 에 `profile` 과 `query` 를 넘겨줍니다.
```
SplunkProfile profile = c.getDocument(SplunkProfile.class);

return new BootcampCommand(profile, query);
```
---

### `BootcampCommand.java`
`DriverQueryCommand` 를 상속합니다. (무슨 클래스인지 추후 확인 ..)
그리고 `BootcampCommandParser` 에서 넘겨준 `SplunkProfile` 과 `query` 를 받는 생성자를 작성합니다.
```
public class BootcampCommand extends DriverQueryCommand {
	private SplunkProfile profile;
	private String query;
	
	public BootcampCommand(SplunkProfile profile, String query) {
		this.profile = profile;
		this.query = query;
	}
}
```

그리고 부모 클래스의 method 들을 오버라이드 해줍니다.
```
@Override
public String getName() { // 커맨드 이름을 리턴
	return "bootcamp";
}
```

```
private Service svc;

@Override
public void onStart() { // 커맨드 실행
	svc = profile.connect();
}
```
커맨드가 실행되면 `this.profile` 을 이용해 Splunk 에 연결합니다.

```
@Override
protected void onClose() { // 커맨드 종료
	if (svc != null)
		svc.logout();
}

@Override
protected void onCancel(QueryCancelReason reason) { // 커맨드 취소
	if (svc != null)
		svc.logout();
}
```
커맨드가 종료되거나 취소되면 `svc.logout()` 을 호출하여 Splunk 와의 연결을 종료합니다.

`run()` 에서 본격적인 작업을 수행합니다.
우선 Splunk 에서 수행할 `Job` 을 하나 전역변수로 잡아둡니다.
```
private Job job;

@Override
public void run() {
	job = null;
}
```

이제 (Splunk) `Service` 에서 `job` 을 받아와 `create` method 로 쿼리를 실행합니다.
`create(String query, JobArgs args)` 의 형태이므로 `Job` 을 실행할 인자들을 설정하기 위해
`JobArgs` 를 생성하고 `ExecutionMode.NORMAL` 로 지정하여 `create` method 에 넘겨줍니다.
```
JobArgs ja = new JobArgs();
ja.setExecutionMode(JobArgs.ExecutionMode.NORMAL);
job = svc.getJobs().create(query, ja);
```

Splunk 에 쿼리가 실행되는 동안 무한루프를 돌고 기다립니다.
```
while (!job.isDone()) {
	try {
		Thread.sleep(500);
	} catch (InterruptedException e) {
	}
}
```

`Job`이 끝나면 `InputStream` 으로 결과를 읽어와야 합니다.

결과의 총 개수와, 현재까지 처리한 결과 수를 `resultCount`, `offset` 에 각각 저장하고,
```
int resultCount = job.getResultCount();
int offset = 0;
```

처리할 결과가 남아있고, 쿼리 실행 중지 요청이 들어오지 않는 동안 결과를 읽어옵니다.
```
while (offset < resultCount && !isCancelRequested()) {
	// get query results
}
```

결과를 읽어올 때는 `job.getResults(Map args)` 로 가져옵니다.
`args` 에 결과를 몇 개씩 가져올지, 몇 번째 결과를 가져올지 설정하여 `getResults` 를 호출합니다.

`CollectionArgs` 에 이 옵션을 다음과 같이 저장합니다.
- `count` : 가져올 결과 개수
- `offset` : 몇 번째 결과부터 가져올지

이렇게 `getResults` 를 호출하면 `InputStream` 이 리턴됩니다. 
```
int count = 500;
InputStream is = null;

while (offset < resultCount && !isCancelRequested()) {
	CollectionArgs outputArgs = new CollectionArgs();
	outputArgs.setCount(count);
	outputArgs.setOffset(offset);

	is = job.getResults(outputArgs);
}
```

`ResultsReaderXml reader` 에 이를 넘겨주어 `reader` 에 `nextEvent` 가 존재하는 동안 계속 읽어옵니다.
```
ResultsReaderXml reader = null;
while (offset < resultCount && !isCancelRequested()) {
	// ... 생략
	reader = new ResultsReaderXml(is);
	
	HashMap<String, String> event = null;
	while ((event = reader.getNextEvent()) != null) {
		// 결과 저장
	}
}
```
읽어온 결과는 `HashMap` 형태로 바꾸어 `pushPipe` 에 `Row` 의 형태로 넘겨주면, 쿼리 결과 창에 읽어온 결과가 입력됩니다.
그리고 당연히 `offset` 을 업데이트 해줍니다.
```
while ((event = reader.getNextEvent()) != null) {
	pushPipe(new Row(new HashMap<String, Object>(event)));
}

offset = offset + count;
```

이제 `reader` 와 `is` 를 닫아야 하므로 `while` 문 안쪽을 `try-catch` 로 감싸고 안전하게 `close` 해줍니다.
```
while (offset < resultCount && !isCancelRequested()) {
	try {
		// ... 생략
	} catch (Throwable t) {
		if (reader != null)
			try {
				reader.close();
			} catch (IOException e) {
			}
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
			}
	}
}
```

이제 `run()` 이 정상적으로 작동하면 `svc` 에서 로그아웃을 해야하므로, `try-finally` 로 감싸고 `svc.logout()` 을 호출합니다. 
```
@Override
public void run() {
	job = null;
	try {
		// ... 생략
	} finally {
		if (svc != null)
			svc.logout();
	}
}
```

또한 `job` 에 대해서도 완료 처리를 해야 하므로 `onClose()`, `onCancel()` 에 다음 코드를 추가합니다.
```
if (job != null) {
	job.finish();
	job = null;
}
```

## BootcampParser

- `SplunkGithubEventParserFactory.java`
- `SplunkGithubEventParser.java`

`SplunkGithubEventParserFactory` 는 UI가 파서 생성을 위해 호출하는 클래스로, 파서의 전체적인 정보를 담고 있습니다.
`SplunkGithubEventParser` 는 실제 파서의 기능을 정의한 클래스입니다.

---

### `SplunkGithubEventParserFactory.java`

파서를 생성하고, 파서의 정보를 담고있는 `SplunkGithubEventParserFactory` 를 생성합니다.
`AbstractLogParserFactory` 를 상속하고 파서의 정보와 관련된 method 들을 작성합니다.
```
@Component(name = "logpresso-splunk-github-event-parser-factory")
@Provides
public class SplunkGithubEventParserFactory extends AbstractLogParserFactory {

}
```

```
@Override
public String getName() { // 파서의 이름을 리턴
	return "splunk-github-event";
}
```

파서의 이름을 표시할 언어 목록을 가져오고, 파서의 이름을 가져옵니다.
```
@Override
public Collection<Locale> getDisplayNameLocales() {
	return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
}

@Override
public String getDisplayName(Locale locale) {
	if(locale != null && locale.equals(Locale.KOREAN))
		return "스플렁크 깃헙 이벤트";
	return "Splunk Github Event";
}
```

파서의 설명을 표시할 언어 목록을 가져오고, 파서의 설명을 가져옵니다.
```
@Override
public Collection<Locale> getDescriptionLocales() {
	return Arrays.asList(Locale.ENGLISH, Locale.KOREAN);
}

@Override
public String getDescription(Locale locale) {
	if (locale != null && locale.equals(Locale.KOREAN))
		return "Splunk 깃헙 이벤트 로그를 파싱합니다.";
	return "Parse Splunk github event logs.";
}
```

파서의 그룹 이름을 언어에 맞게 가져옵니다. 여기서 그룹 이름은 `부트캠프` 로 지정해 둡니다.
```
@Override
public String getDisplayGroup(Locale locale) {
	if (locale != null && locale.equals(Locale.KOREAN))
		return "부트캠프";
	return "Bootcamp";
}
```

마지막으로 파서를 생성하는 method 로, 실제로 UI 에서 파서를 생성할 때 이 method 를 호출합니다.
여기서는 파서를 생성해 리턴해 줍니다.
```
@Override
public LogParser createParser(Map<String, String> configs) {
	return new SplunkGithubEventParser();
}
```

---

### `SplunkGithubEventParser.java`

`V1LogParser` 를 상속합니다. (이유는 ...)
그러면 `parse` 라는 method 하나만 작성해 주면 됩니다.

```
public class SplunkGithubEventParser extends V1LogParser {
	@Override
	public Map<String, Object> parse(Map<String, Object> params) {
		// parse log
	}
}
```

`parse(Map<String, Object> params)` 는 `params` 를 받아서 키 `line` 에 해당하는 값 (`params.get("line")`) 을 파싱하여 얻은 key 와 그에 해당하는 value 들을 map `m` 에 담아 return 해주는 method 입니다.

(현재 커맨드 `bootcamp` 가 `"_raw"` 로 넘겨주는 상황이므로 `"_raw"` 항목을 가져와 파싱합니다.) 
(Splunk 에서 가져올 때 `_raw` 필드로 가져옴)

리턴해줄 map 을 선언하고 파싱할 로그를 가져옵니다.
```
Map<String, Object> m = new HashMap<String, Object>();
String raw = (String) params.get("_raw");
```

로그 형태를 참고하여 `"` 를 기준으로 `split` 한 후 2번째 원소를 가져옵니다.
이제 `<<LF>>\t-` 를 기준으로 한 번 더 `split` 해주면 각 key 별로 분리되어 배열에 저장됩니다.
```
String data = raw.split("\"")[1];
String[] items = data.split("<<LF>>\t- ");
```

이제 `items` 를 순회하며 각 필드를 읽고 저장하면 됩니다.
우선 date 는 `SimpleDateFormat` 을 이용하여 형태에 맞게 `parse` 하여 저장합니다.
```
// Global Variable
private SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

@Override
public Map<String, Object> parse(Map<String, Object> params) {
	// ... 생략
	String date = items[0];
	Date d = null;
	try {
		d = sdf.parse(date);
	} catch (ParseException e) {
	}
	m.put("_time", d);
}
```

나머지 항목들은 `=` 의 index 를 기준으로 
`substring` 을 이용해 앞 부분은 필드명 `field`, 뒷 부분은 필드의 값 `value` 로 간주합니다.

```
for (int i = 1; i < items.length; ++i) {
	int idx = items[i].indexOf('=');
	if (idx < 0) {
		slog.error("parser cannot parse ", items[i]);
		continue;
	}
	String field = items[i].substring(0, idx);
	String value = items[i].substring(idx + 1);
}
```

마지막으로 자료형 타입에 맞도록 특정 필드 이름에 대해서는 형 변환을 해줍니다.
`Long.parseLong(value)`, `Boolean.parseBoolean(value)` 를 이용합니다.
그리고 리턴할 맵 `m` 에 파싱 결과를 넣어줍니다.
```
if (field.equals("_id") || field.equals("repo_id") || field.equals("user_id")) {
	m.put(field, Long.parseLong(value));
} else if (field.equals("public")) {
	m.put(field, Boolean.parseBoolean(value));
} else {
	m.put(field, value);
}
```
이제 파싱한 값들이 담겨있는 map `m` 을 리턴 해줍니다.
```
return m;
```

### metadata.xml 수정

이 과정들을 마치고 `resources/metadata.xml` 에 `<instance component />` 로 `SplunkGithubEventParserFactory` 와 `BootcampCommandParser` 를 등록하면 사용이 가능해집니다.

```
<instance component="logpresso-bootcamp-command-parser" />
<instance component="logpresso-splunk-github-event-parser-factory" />
```


### UI 에 적용

build 한 후 ssh 로 접속해서 `bundle.replace`, `bundle.refresh` 를 해줍니다.

웹으로 들어가서 `시스템 설정` 탭의 `파서` 메뉴로 들어가
새로운 파서를 생성합니다
그러면 `getDisplayGroup` 에서 지정했던 그룹 이름인 `부트캠프`에 작성한 파서가 있음을 확인할 수 있습니다.

파서를 고르고 이름을 `event` 로 지정하겠습니다.
이제 `쿼리` 탭으로 들어가서 `실행한 쿼리` 뒤에 `"| parse event "` 와 같이 쿼리를 붙여주면 파서를 거친 결과가 나옵니다.

Example
```
bootcamp name=splunk query="search index=github" | parse event
```
