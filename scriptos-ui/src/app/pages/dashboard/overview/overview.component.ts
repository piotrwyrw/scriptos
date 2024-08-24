import { Component } from '@angular/core';
import {ChartModule} from "primeng/chart";

@Component({
  selector: 'app-overview',
  standalone: true,
  imports: [
    ChartModule
  ],
  templateUrl: './overview.component.html',
  styleUrl: './overview.component.scss'
})
export class OverviewComponent {

  chartOptions: any = {
    responsive: true,
    maintainAspectRatio: true,
    plugins: {
      legend: {
        display: true
      },
      tooltip: {
        enabled: true,
      }
    }
  }

  diskUsage: any = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr'],
    datasets: [
      {
        cubicInterpolationMode: 'monotone',
        label: 'Disk usage',
        data: [100, 500, 1200, 1250]
      }
    ]
  }

  protected readonly screen = screen;
}
