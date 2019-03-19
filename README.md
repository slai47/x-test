# x-test

## Task 1-4
Above is in the repo.

## Task 5
Love some feedback.

## Backend

### Query

```SQL
SELECT
 device_model,
 count(*)
FROM
 app.data
 WHERE created_at BETWEEN "2018-01-01" AND "2018-01-07" AND created_at > "2018-01-07"
GROUP BY
 device_model;
```

My sqlite is a bit rough and probably doing the time comparison wrong. But this should be about right.
 
### Architecture

Going to take a crack at this but probably not going to even be remotely right.

I would probably build out an api that would collect a select amount of data points. Once hitting their limits memory wise, send that batched data to another layer that would batch insert into the database. The database would probably want to be archived often to remove older data that doesn't need to be in the most current data set. This would help reduce query times but still allow us to analyze the data.


## apk
In the repo, download from the Github Repo