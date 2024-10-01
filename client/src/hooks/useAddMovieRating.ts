import { useMutation, useQueryClient } from "@tanstack/react-query";

interface MovieRatingPayload {
  movieId: string;
  userRating: number;
  token: string;
}

const submitMovieRating = async ({ movieId, userRating, token }: MovieRatingPayload) => {
  await fetch(`http://localhost:8088/movies/${movieId}/ratings`, {
    method: "POST",
    body: JSON.stringify({ rating: userRating }),
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });
};

export const useAddMovieRating = (movieId: string) => {
  const queryClient = useQueryClient();

  const { mutateAsync: rateMovie } = useMutation({
    mutationKey: ["movies", movieId, "ratings"],
    mutationFn: submitMovieRating,
    onSuccess: async () => await queryClient.invalidateQueries({ queryKey: ["movies", movieId] }),
  });

  return { rateMovie };
};
