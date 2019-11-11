import { Component, NgZone } from '@angular/core';
import { QueryService, SubscribeTypes } from 'eediom-sdk';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent {
  title = 'bootcamp';
  query:string = '';
  private _result: any = [];
  public get result(): any {
    return this._result;
  }
  public set result(value: any) {
    this._result = value;
  }
  runQuery:boolean = false;

  constructor(private queryService: QueryService, private ngZone: NgZone) {
  }

  executeQuery() {
    this.queryService.query(this.query, (queryId, subscribeData) => {
      if (subscribeData.type === SubscribeTypes.Eof) {
        this.queryService.getResult(queryId, 100, 0).then((queryResult) => {
          this.ngZone.run(() => {
            this.runQuery = true;
            this.result = queryResult.records;
          })
        })
      }
    });
  }
}
