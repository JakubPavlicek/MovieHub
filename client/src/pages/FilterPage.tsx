import { FC } from "react";

export const FilterPage: FC = () => {
  // const api = useApi();
  // const { data: movies, isError } = api.useQuery("get", "/movies/filter", {
  //   params: {
  //     path: { directorId: directorId },
  //   },
  // });
  //
  // if (!movies?.content) {
  //   return <div>Empty</div>;
  // }

  return (
    <main className="mx-auto min-h-[70vh] 2xl:container">
      <div className="mx-5 mt-10 flex flex-col justify-between text-white">
        <div className="mb-6 inline-flex max-w-fit gap-1.5 border-b-2 border-cyan-400 text-3xl font-semibold">
          {/*<span className="capitalize">'{directorName}'</span>*/}
          <span>movies</span>
        </div>
        {/*<MoviePreviewList movies={movies.content} />*/}
      </div>
    </main>
  );
};
