import { useNavigate, useParams } from "react-router-dom";
import { useApi } from "@/context/ApiProvider";
import { useEffect } from "react";

export const useActorMovies = () => {
  const { actorName } = useParams();
  const navigate = useNavigate();
  const api = useApi();

  const { data: actor, isSuccess } = api.useQuery("get", "/actors", {
    params: {
      query: {
        limit: 1,
        name: actorName,
      },
    },
  });

  const actorId = actor?.content[0]?.id;

  const { data: movies, isError } = api.useQuery(
    "get",
    `/actors/{actorId}/movies`,
    {
      params: {
        path: { actorId: actorId! },
      },
    },
    {
      enabled: isSuccess && actor?.content?.length > 0,
    },
  );

  useEffect(() => {
    if (isError) {
      navigate("/", { replace: true });
    }
  }, [isError, navigate]);

  return { actorName, movies };
};
