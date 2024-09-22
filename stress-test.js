import http from 'k6/http';
import { sleep } from 'k6';

export let options = {
  stages: [
    { duration: '60s', target: 100 },
    { duration: '1m', target: 50 },
    { duration: '15s', target: 0 },
  ],
};

export default function () {
  http.get('http://127.0.0.1:41569/');
  sleep(1);
}