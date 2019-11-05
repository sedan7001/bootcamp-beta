import { Component } from '@angular/core';
import { QueryService } from 'eediom-sdk';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.less']
})
export class AppComponent {
  title = 'test4';
  // query = "bootcamp name=test query='search index=github' | parse event";
  // query = "table sys_cpu_logs";
  query;
  result;

  constructor(private queryService: QueryService) {
  }

  ngOnInit(): void {
    // this.init();
  }


  async init(): Promise<void> {
    this.queryService.query(this.query, 100, 0).then((res) => {
      console.log(res);
      this.result = res.records;
    });
  }
}
