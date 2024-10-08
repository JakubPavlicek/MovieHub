import { useNavigate, useParams } from "react-router-dom";
import { useApi } from "@/context/ApiProvider";
import { useEffect } from "react";

export const useDirectorMovies = () => {
  const { directorName } = useParams();
  const navigate = useNavigate();
  const api = useApi();

  const { data: director, isSuccess } = api.useQuery("get", "/directors", {
    params: {
      query: {
        limit: 1,
        name: directorName,
      },
    },
  });

  const directorId = director?.content[0]?.id;

  const { data: movies, isError } = api.useQuery(
    "get",
    `/directors/{directorId}/movies`,
    {
      params: {
        path: { directorId: directorId! },
      },
    },
    {
      enabled: isSuccess && director?.content?.length > 0,
    },
  );

  useEffect(() => {
    if (isError) {
      navigate("/", { replace: true });
    }
  }, [isError, navigate]);

  return { directorName, movies };
};
